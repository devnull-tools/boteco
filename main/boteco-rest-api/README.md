# Boteco REST API

This module contains a REST API that allows external communication. Once this bundle is started, you can access
a set of paths:

- `POST /subscriptions/{event}/{channel}/{target}`: Subscribe to an event
- `DELETE /subscriptions/{event}/{channel}/{target}`: Unsubscribe from an event
- `POST /confirmations/{token}`: Confirm a request
- `POST /channels/{channel}/{target}`: Send a message
- `GET /channels/channels`: List all available channels
- `POST /events/{event_id}`: Broadcast an event
