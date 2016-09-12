# Boteco Event BUS

This bundle contains an implementation of the `EventBus` that uses JMS topics to broadcast events. All events will be
sent to topics with the pattern "boteco.event.$ID", where $ID is the ID of the event.