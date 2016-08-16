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

package tools.devnull.boteco.plugins.subscription.manager;

import tools.devnull.boteco.event.Subscription;
import tools.devnull.boteco.event.SubscriptionEventSelector;
import tools.devnull.boteco.event.SubscriptionManager;
import tools.devnull.boteco.event.SubscriptionRemovalEventSelector;
import tools.devnull.boteco.event.SubscriptionRemovalTargetSelector;
import tools.devnull.boteco.event.SubscriptionTargetSelector;
import tools.devnull.boteco.message.MessageSender;

import java.util.List;

public class BotecoSubscriptionManager implements SubscriptionManager {

  private final MessageSender messageSender;
  private final SubscriptionRepository repository;

  public BotecoSubscriptionManager(SubscriptionRepository repository, MessageSender messageSender) {
    this.messageSender = messageSender;
    this.repository = repository;
  }

  @Override
  public List<Subscription> subscriptions(String eventId) {
    return repository.find(eventId);
  }

  @Override
  public List<Subscription> subscriptions(String channel, String target) {
    return repository.find(channel, target);
  }

  @Override
  public SubscriptionTargetSelector subscribe() {
    return target -> channel -> new _SubscriptionEventSelector(target, channel);
  }

  @Override
  public boolean confirm(String token) {
    return repository.confirm(token);
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
          String token = repository.requestInsert(eventId, channel, target);
          messageSender.send(String.format("To confirm your subscription to %s, use any channel to send " +
              "a 'subscription confirm' command with this token: %s", eventId, token))
              .to(target)
              .through(channel);
          return BotecoSubscriptionManager.this;
        }

      };
    }

    @Override
    public SubscriptionManager toEvent(String eventId) {
      repository.insert(eventId, channel, target);
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
          String token = repository.requestDelete(eventId, channel, target);
          messageSender.send(String.format("To confirm your subscription removal from %s, use any channel to send " +
              "a 'confirm' command with this token: %s", eventId, token))
              .to(target)
              .through(channel);
          return BotecoSubscriptionManager.this;
        }

      };
    }

    @Override
    public SubscriptionManager fromEvent(String eventId) {
      repository.delete(eventId, channel, target);
      return BotecoSubscriptionManager.this;
    }

  }

}
