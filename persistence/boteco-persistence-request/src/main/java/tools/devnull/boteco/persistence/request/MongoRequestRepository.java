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
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.plugins.request.RequestRepository;
import tools.devnull.boteco.request.Request;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * A repository for requests that persists information in a MongoDB
 */
public class MongoRequestRepository implements RequestRepository {

  private static final Logger logger = LoggerFactory.getLogger(MongoRequestRepository.class);

  private final MongoCollection<Document> requests;
  private final Gson gson;

  public MongoRequestRepository(MongoDatabase database, Long minutesToExpire) {
    this.requests = database.getCollection("requests");
    try {
      this.requests.dropIndex("createdAt_1");
      logger.info("Dropped index 'createdAt', creating a new one.");
    } catch (MongoCommandException e) {
      logger.info("Index for 'createdAt' doesn't exist, creating index.");
    }
    this.requests.createIndex(new Document("createdAt", 1),
        new IndexOptions().expireAfter(minutesToExpire, TimeUnit.MINUTES));
    this.gson = new Gson();
  }

  @Override
  public String create(Object target, String type) {
    String token = UUID.randomUUID().toString();
    Document document = new Document("_id", token)
        .append("createdAt", new Date())
        .append("type", type)
        .append("object", Document.parse(this.gson.toJson(target)));
    this.requests.insertOne(document);
    return token;
  }

  @Override
  public <T> Request<T> pull(String token) {
    Document document = this.requests.findOneAndDelete(new BasicDBObject("_id", token));
    if (document != null) {
      return new BotecoRequest<>(document);
    }
    return null;
  }

}
