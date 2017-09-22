# MongoDB Plugin

This plugin exposes an `com.mongodb.client.MongoDatabase` to accessMongoDB instances.

## How to configure

Just use the config `tools.devnull.boteco.plugins.mongodb`. The supported properties are:

- `db.url`: the url for the MongoDB instance (defaults to `mongodb://localhost:27017/boteco`)
