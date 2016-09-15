# Boteco Telegram Channel

This is the implementation of the Telegram Channel. It allows Message Processors to receive messages from Telegram.

## How to configure

Just use the config `tools.devnull.boteco.channel.telegram`. The supported properties are:

- `bot.token`: the bot token (you get this one after a new bot registration)
- `bot.command.expression`: the regular expression that defines a command (defaults to
`^/(?<command>[^@ ]*)(@\w+bot)?\s*(?<arguments>.+)?`)
- `poll.interval`: the interval in which the channel should polling for new messages (defaults to `'2s'`)
- `poll.initial-offset`: the initial offset to fetch the messages (defaults to `0`)

## How to get your Telegram ID

This channel also comes with a Message Processor to retrieve both user and chat id. Just use the command `id` and
boteco will reply with your user id and the chat id so you can use them as the target for sending messages through the
REST API.
