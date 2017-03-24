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
import tools.devnull.boteco.event.Subscription;
import tools.devnull.boteco.event.SubscriptionManager;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;

import java.util.List;
import java.util.stream.Collectors;

@Command("subscriptions")
@AlwaysActive
public class SubscriptionListMessageProcessor implements MessageProcessor {

  private final SubscriptionManager subscriptionManager;

  public SubscriptionListMessageProcessor(SubscriptionManager subscriptionManager) {
    this.subscriptionManager = subscriptionManager;
  }

  @Override
  public void process(IncomeMessage message) {
    SubscriptionListParameters parameters = message.command().as(SubscriptionListParameters.class);
    String channel = parameters.channel();
    String target = parameters.target();
    List<Subscription> subscriptions = this.subscriptionManager.subscriptions(channel, target);
    if (subscriptions.isEmpty()) {
      message.reply("No subscriptions");
    } else {
      message.reply(subscriptions.stream()
          .map(Subscription::eventId)
          .collect(Collectors.joining("\n")));
    }
  }

}
