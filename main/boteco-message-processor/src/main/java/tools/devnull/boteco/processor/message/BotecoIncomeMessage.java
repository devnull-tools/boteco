package tools.devnull.boteco.processor.message;

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.Message;
import tools.devnull.boteco.message.MessageCommand;
import tools.devnull.boteco.message.MessageSender;
import tools.devnull.boteco.Sendable;
import tools.devnull.boteco.message.Sender;
import tools.devnull.boteco.user.User;

import javax.validation.Validator;

public class BotecoIncomeMessage implements IncomeMessage {

  private final ServiceRegistry registry;
  private final Message message;

  public BotecoIncomeMessage(ServiceRegistry registry, Message message) {
    this.registry = registry;
    this.message = message;
  }

  @Override
  public Channel channel() {
    return message.channel();
  }

  @Override
  public String content() {
    return message.content();
  }

  @Override
  public Sender sender() {
    return message.sender();
  }

  @Override
  public User user() {
    return message.user();
  }

  @Override
  public String target() {
    return message.target();
  }

  @Override
  public boolean isPrivate() {
    return message.isPrivate();
  }

  @Override
  public boolean isGroup() {
    return message.isGroup();
  }

  @Override
  public String replyTo() {
    return message.replyTo();
  }

  @Override
  public boolean hasCommand() {
    return channel().commandExtractor().isCommand(this);
  }

  @Override
  public MessageCommand command() {
    return new ValidatableMessageCommand(registry.locate(Validator.class).one(),
        channel().commandExtractor().extract(this));
  }

  @Override
  public void reply(Sendable object) {
    getMessageSender()
        .send(object)
        .replyingTo(isPrivate() ? null : replyTo())
        .to(target())
        .through(channel().id());
  }

  @Override
  public void sendBack(Sendable object) {
    getMessageSender()
        .send(object)
        .to(target())
        .through(channel().id());
  }

  private MessageSender getMessageSender() {
    return registry.locate(MessageSender.class).one();
  }

}
