# Boteco Email Channel

This is the implementation of the Email Channel. It allows Message Processors to receive messages from Email.

## How to configure

Just use the config `tools.devnull.boteco.channel.email`. The supported properties are:

**Income Mail**
- `income.protocol`: the protocol to use for income emails (defaults to `imap`)
- `income.host`: the mail host to use, the port would go here if not the default (defaults to `localhost`)
- `income.options`: additional options for the Camel Mail component

**Outcome Mail**
- `outcome.protocol`: the protocol to use for outcome emails (defaults to `imap`)
- `outcome.host`: the mail host to use, the port would go here if not the default (defaults to `localhost`)
- `outcome.options`: additional options for the Camel Mail component
- `outcome.from`: the sender email to use
