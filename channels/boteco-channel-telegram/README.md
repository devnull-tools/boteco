# Boteco Telegram Channel

This is the implementation of the Telegram Channel. It allows Message Processors to receive messages from Telegram.

## How to configure

Just use the config `tools.devnull.boteco.channel.telegram`. The supported properties are:

- `income.enabled`: if the channel should process incoming messages (defaults to `false`)
- `outcome.enabled`: if the channel should process outcome messages (defaults to `false`)
- `bot.token`: the bot token (you get this one after a new bot registration)
- `bot.username`: the bot username on telegram (defaults to `boteco_bot`)
- `poll.interval`: the interval in which the channel should polling for new messages (defaults to `'500'` - milliseconds)
- `poll.initial-offset`: the initial offset to fetch the messages (defaults to `0`)

## How to get your Telegram ID

This channel also comes with a Message Processor to retrieve both user and chat id. Just use the command `id` and
boteco will reply with your user id and the chat id so you can use them as the target for sending messages through the
REST API.
