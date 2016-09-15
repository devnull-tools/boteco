# Boteco IRC Channel

This is the implementation of the IRC Channel. It allows Message Processors to receive messages from IRC.

## How to configure

Just use the config `tools.devnull.boteco.channel.irc`. The supported properties are:

- `irc.nick`: the nickname to use (defaults to `'boteco'`)
- `irc.server`: the server to connect (defaults to `'localhost'`)
- `irc.port`: the port to connect (defaults to `'6667'`)
- `bot.command.prefix`: the regular expression that defines the command prefix (defaults to `'^boteco[,:]?\s+'`)

## How to add channels

Channels are added through the classical `/invite` irc command. Just invite the bot and it will join the channel
and remember that channel so it can join again in case of a restart. If you kick boteco from a channel, it will be
removed and boteco will not join it anymore.

## Invite and Kick Events

Every time you invite boteco to a channel or kick it from one, an event is sent and you can subscribe to it using
boteco's subscription system. The events are:

- `irc.invited`: when boteco is invited to join a channel, it will tell you which channel and who invited
- `irc.kicked`: when boteco is kicked from a channel, it will tell you which channel and who kicked

## Ignore List

This channel has a Message Processor that allows you to ignore nicknames. The commands are:

- `ignore-list add <nickname>`: adds a nickname to the ignore list
- `ignore-list remove <nickname>`: removes a nickname from the ignore list
- `ignore-list`: lists the ignored nicknames
