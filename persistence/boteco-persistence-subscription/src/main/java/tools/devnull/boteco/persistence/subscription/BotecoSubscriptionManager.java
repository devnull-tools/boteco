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
import tools.devnull.boteco.event.Subscription;
import tools.devnull.boteco.event.SubscriptionEventSelector;
import tools.devnull.boteco.event.SubscriptionManager;
import tools.devnull.boteco.event.SubscriptionRemovalEventSelector;
import tools.devnull.boteco.event.SubscriptionRemovalTargetSelector;
import tools.devnull.boteco.event.SubscriptionTargetSelector;
import tools.devnull.boteco.message.MessageSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BotecoSubscriptionManager implements SubscriptionManager {

  private final MongoCollection<Document> subscriptions;
  private final MongoCollection<Document> requests;
  private final MessageSender messageSender;
  private final Gson gson;

  public BotecoSubscriptionManager(MongoDatabase database, MessageSender messageSender) {
    this.subscriptions = database.getCollection("subscriptions");
    this.requests = database.getCollection("subscriptionRequests");
    this.messageSender = messageSender;
    this.gson = new Gson();
  }

  @Override
  public List<Subscription> subscriptions(String eventId) {
    List<Subscription> result = new ArrayList<>();
    FindIterable<Document> documents = this.subscriptions.find(new BasicDBObject("eventId", eventId));
    documents.forEach(
        (Consumer<Document>) document -> result.add(gson.fromJson(document.toJson(), BotecoSubscription.class))
    );
    return result;
  }

  @Override
  public SubscriptionTargetSelector subscribe() {
    return target -> channel -> new _SubscriptionEventSelector(target, channel);
  }

  @Override
  public void confirm(String token) {
    Document document = this.requests.find(new BasicDBObject("token", token)).first();
    if (document != null) {
      BotecoSubscriptionRequest request = this.gson.fromJson(document.toJson(), BotecoSubscriptionRequest.class);
      if (request.confirmationToken().equals(token)) {
        switch (request.operation()) {
          case "insert":
            this.subscriptions.insertOne(Document.parse(gson.toJson(request.subscription())));
            this.requests.deleteOne(document);
            break;
          case "delete":
            this.subscriptions.deleteOne(Document.parse(gson.toJson(request.subscription())));
            this.requests.deleteOne(document);
            break;
        }
      }
    }
  }

  @Override
  public SubscriptionRemovalTargetSelector unsubscribe() {
    return target -> channel -> new _SubscriptionRemovalEventSelector(target, channel);
  }

  private class _SubscriptionEventSelector implements SubscriptionEventSelector {

    private final String target;
    private final String channel;

    private _SubscriptionEventSelector(String target, String channel) {
      this.target = target;
      this.channel = channel;
    }

    @Override
    public SubscriptionEventSelector withConfirmation() {
      return new SubscriptionEventSelector() {

        @Override
        public SubscriptionEventSelector withConfirmation() {
          return this;
        }

        @Override
        public SubscriptionManager toEvent(String eventId) {
          BotecoSubscriptionRequest request = new BotecoSubscriptionRequest(
              new BotecoSubscription(eventId, new BotecoSubscriber(target, channel)), "insert"
          );
          requests.insertOne(Document.parse(gson.toJson(request)));
          messageSender.send(String.format("To confirm your subscription to %s, use any channel to send " +
              "a 'subscription confirm' command with this token: %s", eventId, request.confirmationToken()))
              .to(target)
              .through(channel);
          return BotecoSubscriptionManager.this;
        }

      };
    }

    @Override
    public SubscriptionManager toEvent(String eventId) {
      Subscription subscription = new BotecoSubscription(eventId, new BotecoSubscriber(target, channel));
      subscriptions.insertOne(Document.parse(gson.toJson(subscription)));
      return BotecoSubscriptionManager.this;
    }

  }

  private class _SubscriptionRemovalEventSelector implements SubscriptionRemovalEventSelector {

    private final String target;
    private final String channel;

    private _SubscriptionRemovalEventSelector(String target, String channel) {
      this.target = target;
      this.channel = channel;
    }

    @Override
    public SubscriptionRemovalEventSelector withConfirmation() {
      return new SubscriptionRemovalEventSelector() {

        @Override
        public SubscriptionRemovalEventSelector withConfirmation() {
          return this;
        }

        @Override
        public SubscriptionManager fromEvent(String eventId) {
          BotecoSubscriptionRequest request = new BotecoSubscriptionRequest(
              new BotecoSubscription(eventId, new BotecoSubscriber(target, channel)), "delete"
          );
          requests.insertOne(Document.parse(gson.toJson(request)));
          messageSender.send(String.format("To confirm your subscription removal from %s, use any channel to send " +
              "a 'confirm' command with this token: %s", eventId, request.confirmationToken()))
              .to(target)
              .through(channel);
          return BotecoSubscriptionManager.this;
        }

      };
    }

    @Override
    public SubscriptionManager fromEvent(String eventId) {
      Subscription subscription = new BotecoSubscription(eventId, new BotecoSubscriber(target, channel));
      subscriptions.deleteOne(Document.parse(gson.toJson(subscription)));
      return BotecoSubscriptionManager.this;
    }

  }

}
