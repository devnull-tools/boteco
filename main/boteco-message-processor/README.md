# Boteco Message Processor

This bundle contains the logic to dispatch messages to the message processors that can process them. The messages
are filtered using `Rule` objects and the allowed ones sent to the `boteco.process.${channel_id}` queue.
