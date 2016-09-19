# Boteco Rest Client

This is the bundle that implements the Rest Client DSL.

## How to configure

Just use the config `tools.devnull.boteco.client.rest`. The supported properties are:

- `error.retries`: the number of retries to do in case of a connection error (defaults to `0`)
- `timeout.value`: the timeout in milliseconds (defaults to `10000`)
- `retry.interval`: the wait time in milliseconds before retrying the connection (defaults to `500`)
