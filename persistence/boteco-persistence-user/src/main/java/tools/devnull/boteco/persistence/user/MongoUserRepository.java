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

package tools.devnull.boteco.persistence.user;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tools.devnull.boteco.MessageDestination;
import tools.devnull.boteco.user.User;
import tools.devnull.boteco.user.UserAlreadyExistException;
import tools.devnull.boteco.plugins.user.UserRepository;

public class MongoUserRepository implements UserRepository {

  private final MongoCollection<Document> users;
  private final Gson gson;

  public MongoUserRepository(MongoDatabase database) {
    this.users = database.getCollection("users");
    this.gson = new Gson();
  }

  @Override
  public User find(MessageDestination destination) {
    Document document = this.users.find(
        new BasicDBObject("destinations." + destination.channel(), destination.target())
    ).iterator().next();
    if (document != null) {
      return gson.fromJson(document.toJson(), BotecoUser.class);
    }
    return null;
  }

  @Override
  public User find(String userId) {
    Document document = this.users.find(new BasicDBObject("_id", userId)).iterator().next();
    if (document != null) {
      return gson.fromJson(document.toJson(), BotecoUser.class);
    }
    return null;
  }

  @Override
  public User create(String userId, MessageDestination defaultDestination) {
    if (find(userId) != null) {
      throw new UserAlreadyExistException();
    }
    User user = new BotecoUser(userId, defaultDestination);
    this.users.insertOne(Document.parse(this.gson.toJson(user)));
    return user;
  }

  @Override
  public void update(User user) {
    BasicDBObject query = new BasicDBObject();
    query.put("_id", user.id());
    Document update = Document.parse(this.gson.toJson(user));
    update.remove("_id");
    this.users.updateOne(query, new Document("$set", update));
  }

}
