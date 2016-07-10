# Boteco IRC Channel

This is the implementation of the IRC Channel. It allows Message Processors to receive messages from IRC.

## How to configure

Just use the config `tools.devnull.boteco.channel.irc`. The supported properties are:

- `irc.nick`: the nickname to use (defaults to `'boteco'`)
- `irc.server`: the server to connect (defaults to `'localhost'`)
- `irc.port`: the port to connect (defaults to `'6667'`)
- `bot.ignorelist`: the nicks separated by commas that the bot should ignore (defaults to `''`)
- `bot.comman.expression`: the regular expression that defines a command (defaults to `'^boteco[,:]?\s+'`)
- `irc.options`: the camel irc options to use

## How to add channels

Channels are added through the classical `/invite` irc command. Just invite the bot and it will join the channel
and remember that channel so it can join again in case of a restart.
