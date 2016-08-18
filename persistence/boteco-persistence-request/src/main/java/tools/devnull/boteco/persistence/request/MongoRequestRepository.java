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

package tools.devnull.boteco.persistence.request;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import tools.devnull.boteco.Request;
import tools.devnull.boteco.plugins.request.RequestRepository;

import java.util.function.Function;

/**
 * A repository for requests that persists information in a MongoDB
 */
public class MongoRequestRepository implements RequestRepository {

  private final MongoCollection<Document> requests;
  private final Gson gson;

  public MongoRequestRepository(MongoDatabase database) {
    this.requests = database.getCollection("requests");
    this.gson = new Gson();
  }

  @Override
  public void save(Request request) {
    Document document = new Document("token", request.token());
    document.put("type", request.object().getClass().toString());
    document.put("object", Document.parse(this.gson.toJson(request.object())));
    this.requests.insertOne(document);
  }

  @Override
  public <T> T find(String token, Class<T> objectType) {
    return operate(token, objectType, query -> this.requests.find(query).iterator().next());
  }

  @Override
  public <T> T delete(String token, Class<T> objectType) {
    return operate(token, objectType, this.requests::findOneAndDelete);
  }

  private <T> T operate(String token, Class<T> objectType, Function<Bson, Document> function) {
    BasicDBObject query = new BasicDBObject("$and", new BasicDBObject[]{
        new BasicDBObject("token", token),
        new BasicDBObject("type", objectType.toString())
    });
    Document document = function.apply(query);
    if (document != null) {
      return this.gson.fromJson(document.get("object", Document.class).toJson(), objectType);
    }
    return null;
  }

}
