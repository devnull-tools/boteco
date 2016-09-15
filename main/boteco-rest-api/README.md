# Boteco REST API

This module contains a REST API that allows external communication. Once this bundle is started, you can access
a set of paths:

- `POST /subscription/{channel}/{target}/{event}`: Subscribe to an event
- `DELETE /subscription/{channel}/{target}/{event}`: Unsubscribe from an event
- `POST /confirmation/{token}`: Confirm a request
- `POST /message/{channel}/{target}`: Send a message
- `GET /message/channels`: List all available channels
- `POST /event/{event_id}`: Broadcast an event
