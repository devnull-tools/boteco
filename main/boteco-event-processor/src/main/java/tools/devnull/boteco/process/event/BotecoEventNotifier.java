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

package tools.devnull.boteco.process.event;

import tools.devnull.boteco.event.Event;
import tools.devnull.boteco.event.EventListener;
import tools.devnull.boteco.event.SubscriptionManager;
import tools.devnull.boteco.message.MessageSender;

public class BotecoEventNotifier implements EventListener {

  private final SubscriptionManager subscriptionManager;
  private final MessageSender messageSender;

  public BotecoEventNotifier(SubscriptionManager subscriptionManager, MessageSender messageSender) {
    this.subscriptionManager = subscriptionManager;
    this.messageSender = messageSender;
  }

  @Override
  public void onEvent(Event event) {
    this.subscriptionManager.subscriptions(event.id())
        .forEach(subscription -> messageSender.send(event.object().message())
            .to(subscription.subscriber().target())
            .through(subscription.subscriber().channel()));
  }

}