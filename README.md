# Boteco

What if you could write a bot that works on every chat platform? Boteco is a set of abstractions that allows you to write a bot that has the same behaviour across different platforms.

Boteco is gladly built on top of JBoss Fuse.

## How to build

### Pre Requisites

- Maven
- JDK 8

### Build Step

Just do the classical Maven command:

~~~
$ mvn install
~~~

## How to run

### Pre Requisites

If you want to run boteco with all bundled plugins, make sure you have at least a MongoDB running in localhost:27017 or
create a configuration named `tools.devnull.boteco.plugins.mongodb` with the following properties:

- `db.url`: the mongodb url to connect to the mongodb instance
- `db.database`: the mongodb database to retrieve collections

### Running in JBoss Fuse

It's easier than fall of a bike! From your Fuse console, do the following commands (`$VERSION` is the version of
boteco that you want to install):

~~~
features:addurl mvn:tools.devnull/boteco-features/$VERSION/xml/features

features:install boteco-all
~~~

Built in channels will start automatically but you need to configure them correctly. See the channel projects for
details about how to configure each one. You can also install the feature `boteco` to install the base platform and
install the feature for each channel separately:

- `boteco-irc`
- `boteco-pushover`
- `boteco-telegram`
- `boteco-email`
- `boteco-user`

Boteco targets JBoss Fuse _6.3.0_.

## Concepts

Boteco consists basically on the following components:

- **Channel**: a channel is responsible for doing the communication between the Bot and the chat platform.
- **IncomeMessage**: represents a content received from a channel.
- **MessageProcessor**: process income messages.

The flow is pretty simple: a content arrives through a channel, is passed to a processor and the response is send back through the same channel. The figure bellow illustrates the flow:

![Overview](./assets/overview.png)

But how messages from different channels can be processed by the same processor? Let's find out!

### Message Processor

A Message Processor is a class that implements the _MessageProcessor_ interface:

~~~java
public class PingMessageProcessor implements MessageProcessor {

  // Checks if the processor can process a message
  public boolean canProcess(IncomeMessage message) {
    // Built in DSL to help you write the check code
    return check(message).accept(command("ping"));
  }

  // Process the given message
  public void process(IncomeMessage message) {
    // Reply to the message without worrying how to send it
    message.reply("pong");
  }

}
~~~

You can also use the built-in annotations to simplify the code:

~~~java
@Command("ping")
public class PingMessageProcessor implements MessageProcessor {

  // Process the given message
  public void process(IncomeMessage message) {
    // Reply to the message without worrying how to send it
    message.reply("pong");
  }

}
~~~

Both codes will run exactly the same way.

### Channel

Your Message Processor doesn't need to know how to send a content, but the Channel needs. A Channel is the integration between the chat platform and the Boteco runtime, a _camel route_ is the most obvious thought.

A Channel needs to receive a content, wraps it in an instance of `Message` (a lightweight `IncomeMessage`) and then
send to the `MessageProcessor`.

Boteco comes with some channel implementations. Look for the `channels` folder to see how they are implemented and how
you can configure them.

### Routing to Message Processor

When you write a channel, you need to pass the income content to be processed (or not). Instead of writing the code to
find the Message Processor, you can use a MessageDispatcher to do the work. Boteco comes with a OSGi bundle that uses
the OSGi Registry to discover the Message Processors, allowing you to _hot-deploy_ a Message Processor.

To send an income message for processing you just need a reference to a `tools.devnull.boteco.message.MessageDispatcher`
service. The default implementation (in `boteco-message-processor` module) sends the message to a processing queue.
