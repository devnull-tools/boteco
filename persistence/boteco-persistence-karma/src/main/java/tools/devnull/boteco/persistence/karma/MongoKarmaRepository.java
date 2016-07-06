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

package tools.devnull.boteco.persistence.karma;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tools.devnull.boteco.plugins.karma.Karma;
import tools.devnull.boteco.plugins.karma.KarmaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MongoKarmaRepository implements KarmaRepository {

  private static final int ASC = 1;
  private static final int DESC = -1;

  private final MongoCollection<Document> karmas;
  private final Gson gson;

  public MongoKarmaRepository(MongoDatabase database) {
    this.karmas = database.getCollection("karmas");
    this.gson = new Gson();
  }

  @Override
  public Karma find(String term) {
    BasicDBObject query = new BasicDBObject();
    query.put("_id", term.toLowerCase());
    Document result = karmas.find(query).first();
    if (result == null) {
      Karma newKarma = new Karma(term);
      karmas.insertOne(Document.parse(gson.toJson(newKarma)));
      return newKarma;
    }
    return gson.fromJson(result.toJson(), Karma.class);
  }

  @Override
  public void update(Karma karma) {
    BasicDBObject query = new BasicDBObject();
    query.put("_id", karma.name());
    if (karma.value() != 0) {
      karmas.updateOne(query, new Document("$set", new Document().append("value", karma.value())));
    } else {
      // remove karma if it's value is zero (something like a garbage collector)
      karmas.findOneAndDelete(query);
    }
  }

  @Override
  public List<Karma> search(String queryString) {
    int order = DESC;
    List<Karma> result = new ArrayList<>();
    BasicDBObject query = new BasicDBObject();
    queryString = queryString.toLowerCase();
    if (queryString.startsWith("!")) {
      queryString = queryString.substring(1);
      order = ASC;
    }
    if (queryString.contains("*")) {
      query.put("_id", new BasicDBObject("$regex", queryString.replace("*", ".*")).append("$options", "i"));
    } else {
      query.put("_id", queryString);
    }
    FindIterable<Document> documents = karmas.find(query).sort(new BasicDBObject().append("value", order)).limit(10);
    documents.forEach((Consumer<Document>) document -> result.add(gson.fromJson(document.toJson(), Karma.class)));
    return result;
  }
}
