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

package tools.devnull.boteco.persistence.subscription;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tools.devnull.boteco.request.RequestManager;
import tools.devnull.boteco.event.Subscription;
import tools.devnull.boteco.plugins.subscription.SubscriptionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MongoSubscriptionRepository implements SubscriptionRepository {

  private final RequestManager requestManager;
  private final MongoCollection<Document> subscriptions;
  private final Gson gson;

  private final Map<String, Consumer<BotecoSubscriptionRequest>> actionMap;

  public MongoSubscriptionRepository(MongoDatabase database, RequestManager requestManager) {
    this.subscriptions = database.getCollection("subscriptions");
    this.requestManager = requestManager;
    this.gson = new Gson();

    this.actionMap = new HashMap<>();

    this.actionMap.put("insert", this::confirmInsert);
    this.actionMap.put("delete", this::confirmDelete);
  }

  private void confirmInsert(BotecoSubscriptionRequest request) {
    this.subscriptions.insertOne(Document.parse(gson.toJson(request.subscription())));
  }

  private void confirmDelete(BotecoSubscriptionRequest request) {
    this.subscriptions.deleteOne(Document.parse(gson.toJson(request.subscription())));
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
  public boolean confirm(String token) {
    BotecoSubscriptionRequest request = this.requestManager.pull(token, BotecoSubscriptionRequest.class);
    if (request != null) {
      Consumer<BotecoSubscriptionRequest> action = actionMap.get(request.operation());
      if (action != null) {
        action.accept(request);
        return true;
      }
    }
    return false;
  }

  @Override
  public String requestInsert(String eventId, String channel, String target) {
    return createRequest(eventId, channel, target, "insert");
  }

  @Override
  public String requestDelete(String eventId, String channel, String target) {
    return createRequest(eventId, channel, target, "delete");
  }

  private String createRequest(String eventId, String channel, String target, String operation) {
    BotecoSubscriptionRequest subscription = new BotecoSubscriptionRequest(
        new BotecoSubscription(eventId, new BotecoSubscriber(target, channel)), operation
    );
    return requestManager.request(subscription).token();
  }

  @Override
  public void insert(String eventId, String channel, String target) {
    Subscription subscription = new BotecoSubscription(eventId, new BotecoSubscriber(target, channel));
    subscriptions.insertOne(Document.parse(gson.toJson(subscription)));
  }

  @Override
  public void delete(String eventId, String channel, String target) {
    Subscription subscription = new BotecoSubscription(eventId, new BotecoSubscriber(target, channel));
    subscriptions.deleteOne(Document.parse(gson.toJson(subscription)));
  }

}
