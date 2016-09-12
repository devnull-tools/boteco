# Boteco User Channel

This is the implementation of the User Channel. It uses the user's preferred channel for sending messages.

## How to configure

Just use the config `tools.devnull.boteco.channel.user`. The supported properties are:

- `delivery.consumers`: how much consumers will be present to dispatch messages back to the irc (defaults to `10`)
