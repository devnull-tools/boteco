# Boteco Pushover Channel

This is the implementation of the Pushover Channel. It allows delivery through Pushover. This is useful to create
alerts and notifications.

## How to configure

Just use the config `tools.devnull.boteco.channel.pushover`. The supported properties are:

- `pushover.token`: your pushover token
- `delivery.consumers`: how much consumers will be present to dispatch messages back to the irc (defaults to `10`)
