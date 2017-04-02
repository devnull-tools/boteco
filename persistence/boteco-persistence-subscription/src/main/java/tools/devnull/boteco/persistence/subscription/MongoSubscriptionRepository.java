/*
 * The MIT License
 *
 * Copyright (c) 2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.boteco.persistence.subscription;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tools.devnull.boteco.UserMessageLocation;
import tools.devnull.boteco.event.Subscription;
import tools.devnull.boteco.plugins.subscription.SubscriptionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MongoSubscriptionRepository implements SubscriptionRepository {

  private final MongoCollection<Document> subscriptions;
  private final Gson gson;

  public MongoSubscriptionRepository(MongoDatabase database) {
    this.subscriptions = database.getCollection("subscriptions");
    this.gson = new Gson();
  }

  @Override
  public List<Subscription> find(String eventId) {
    List<Subscription> result = new ArrayList<>();
    FindIterable<Document> documents = this.subscriptions.find(new BasicDBObject("eventId", eventId));
    documents.forEach(
        (Consumer<Document>) document -> result.add(gson.fromJson(document.toJson(), BotecoSubscription.class))
    );
    return result;
  }

  @Override
  public List<Subscription> find(String channel, String target) {
    List<Subscription> result = new ArrayList<>();
    FindIterable<Document> documents = this.subscriptions.find(
        new BasicDBObject("$and", new BasicDBObject[]{
            new BasicDBObject("subscriber.channel", channel),
            new BasicDBObject("subscriber.target", target)
        }));
    documents.forEach(
        (Consumer<Document>) document -> result.add(gson.fromJson(document.toJson(), BotecoSubscription.class))
    );
    return result;
  }

  @Override
  public void insert(String eventId, String channel, String target) {
    Subscription subscription = new BotecoSubscription(eventId, new UserMessageLocation(channel, target));
    subscriptions.insertOne(Document.parse(gson.toJson(subscription)));
  }

  @Override
  public void delete(String eventId, String channel, String target) {
    Subscription subscription = new BotecoSubscription(eventId, new UserMessageLocation(channel, target));
    subscriptions.deleteOne(Document.parse(gson.toJson(subscription)));
  }

}
