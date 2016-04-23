/*
 * The MIT License
 *
 * Copyright (c) 2016 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
 *
 * Permission  is hereby granted, free of charge, to any person obtaining
 * a  copy  of  this  software  and  associated  documentation files (the
 * "Software"),  to  deal  in the Software without restriction, including
 * without  limitation  the  rights to use, copy, modify, merge, publish,
 * distribute,  sublicense,  and/or  sell  copies of the Software, and to
 * permit  persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * The  above  copyright  notice  and  this  permission  notice  shall be
 * included  in  all  copies  or  substantial  portions  of the Software.
 *
 * THE  SOFTWARE  IS  PROVIDED  "AS  IS",  WITHOUT  WARRANTY OF ANY KIND,
 * EXPRESS  OR  IMPLIED,  INCLUDING  BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN  NO  EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM,  DAMAGES  OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT  OR  OTHERWISE,  ARISING  FROM,  OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE   OR   THE   USE   OR   OTHER   DEALINGS  IN  THE  SOFTWARE.
 */

package tools.devnull.boteco.plugins.mongodb;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tools.devnull.boteco.storage.ObjectStorage;
import tools.devnull.boteco.storage.RemoveOperation;
import tools.devnull.boteco.storage.RetrieveOperation;
import tools.devnull.boteco.storage.Storable;
import tools.devnull.boteco.storage.StoreOperation;
import tools.devnull.boteco.storage.ValueSelector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * An implementation of an object store that uses MongoDB
 */
public class MongodbObjectStorage implements ObjectStorage {

  private final MongoClient client;
  private final MongoDatabase database;

  public MongodbObjectStorage(String uri, String database) {
    this.client = new MongoClient(new MongoClientURI(uri));
    this.database = client.getDatabase(database);
  }

  @Override
  public <T extends Storable> StoreOperation store(Class<T> type) {
    return (StoreOperation<T>) store -> value -> {
      MongoCollection<Document> collection = database.getCollection(store);
      Gson gson = new Gson();
      Document document = Document.parse(gson.toJson(value));
      if (retrieve(type).from(store).id(value.id()) != null) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", value.id());
        document.remove("_id");
        collection.updateOne(query, new Document("$set", document));
      } else {
        collection.insertOne(document);
      }
    };
  }

  @Override
  public <T extends Storable> RetrieveOperation retrieve(Class<T> type) {
    return (RetrieveOperation<T>) store -> new ValueSelector<T>() {
      @Override
      public T id(Serializable id) {
        MongoCollection<Document> collection = database.getCollection(store);
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        Document result = collection.find(query).first();
        if (result == null) {
          return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(result.toJson(), type);
      }

      @Override
      public List<T> all() {
        List<T> result = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection(store);
        Gson gson = new Gson();
        collection.find().forEach((Consumer<Document>) document ->
            result.add(gson.fromJson(document.toJson(), type))
        );
        return result;
      }

    };
  }

  @Override
  public <T extends Storable> RemoveOperation<T> remove(Class<T> type) {
    return store -> new ValueSelector<T>() {
      @Override
      public T id(Serializable id) {
        MongoCollection<Document> collection = database.getCollection(store);
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);
        Document delete = collection.findOneAndDelete(query);
        return new Gson().fromJson(delete.toJson(), type);
      }

      @Override
      public List<T> all() {
        List<T> list = retrieve(type).from(store).all();
        MongoCollection<Document> collection = database.getCollection(store);
        BasicDBObject document = new BasicDBObject();
        collection.deleteMany(document);
        return list;
      }

    };
  }
}
