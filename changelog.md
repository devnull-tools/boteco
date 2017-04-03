# Changelog

## v0.6.0 (2017-04-02)

- `IncomeMessage` now doesn't need to be implement by channels, only a smaller `Message` interface
- More tests added (please don't freak out, I'll add even more later... I hope)
- Chuck Norris plugin
- Features file is now more organized
- Added a shutdown time for Camel routes
- Added an invocation rule
- Added a way to disable plugins for specific channels
- Project reorganization
- Subscription commands now take multiple events separated by commas
- Included a parser for the RSS from `status.redhat.com` in order to broadcast events
- Added hibernate validator to validate command parameter objects

## v0.5.0 (2017-02-16)

- Improvements on Rest Client DSL
- Added support for title and url in messages and events
- Internal code refactoring

## v0.4.0 (2017-01-04)

- Merged `ServiceLocator` and `ServiceRegister` into a new interface `ServiceRegistry`
- Added trace options for camel routes in channels
- Telegram content formatter
- Link expression changed to `title|url` instead of `title <url>`

## v0.3.0 (2016-12-16)

- Subscription commands simplified (`subscribe`, `unsubscribe` and `subscriptions`)
- Removed irc disconnect notifications (this time for real)
- Improved JmsClient DSL

## v0.2.0 (2016-11-18)

- Code improvements
- Added a way of reconnecting to the irc server in case of a connection error
- Removed irc disconnect notifications

## v0.1.0 (2016-11-13)

- First Release
