# Boteco Channels

Boteco process messages without worrying the source channel. In order to provide this abstraction for Message 
Processors, it's necessary that every channel implements the interfaces `Channel` and `IncomeMessage`.

Any `IncomeMessage` needs to be dispatched through a `MessageDispatcher` to allow it to be processed by the Message 
Processors.

Those projects are implementations for the built-in channels:

- `boteco-channel-email`: an email channel, useful as a notification channel
- `boteco-channel-irc`: an IRC channel for creating an IRC bot (also useful for IRC's channels notifications)
- `boteco-channel-pushover`: a Pushover channel for delivering notifications
- `boteco-channel-telegram`: a Telegram channel for creating Telegram bots
- `boteco-channel-user`: the channel that uses the user's preferred channel to delivery messages