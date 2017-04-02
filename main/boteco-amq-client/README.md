# Boteco AMQ Client

This is the bundle which contains an implementation for the `JmsClient` interface that works for ActiveMQ connections.

## How to configure

Just use the config `tools.devnull.boteco.client.jms`. The supported properties are:

- `jms.user`: the broker user (defaults to `admin`)
- `jms.password`: the broker password (defaults to `admin`)
- `jms.connectionUrl`: the broker url (defaults to `tcp://localhost:61616`)
