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

package tools.devnull.boteco.plugins.subscription;

import tools.devnull.boteco.AlwaysActive;
import tools.devnull.boteco.event.SubscriptionManager;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;

@Command("subscribe")
@AlwaysActive
public class SubscriptionAddMessageProcessor implements MessageProcessor {

  private final SubscriptionManager subscriptionManager;

  public SubscriptionAddMessageProcessor(SubscriptionManager subscriptionManager) {
    this.subscriptionManager = subscriptionManager;
  }

  @Override
  public void process(IncomeMessage message) {
    SubscriptionParameters parameters = message.command().as(SubscriptionParameters.class);
    parameters.each(event -> {
      boolean alreadyRegistered = this.subscriptionManager.subscriptions(event).stream()
          .anyMatch(subscription -> subscription.subscriber().channel().equals(parameters.channel()) &&
              subscription.subscriber().target().equals(parameters.target()));
      if (alreadyRegistered) {
        message.reply("Subscriber has already subscribed to [aa]%s[/aa]", event);
      } else {
        if (parameters.shouldRequestConfirmation()) {
          this.subscriptionManager
              .subscribe()
              .target(parameters.target())
              .ofChannel(parameters.channel())
              .withConfirmation()
              .toEvent(event);
          message.reply("The subscription [aa]%s[/aa] will be added after confirmation!", event);
        } else {
          this.subscriptionManager
              .subscribe()
              .target(parameters.target())
              .ofChannel(parameters.channel())
              .toEvent(event);
          message.reply("Subscription [aa]%s[/aa] added!", event);
        }
      }
    });
  }

}
