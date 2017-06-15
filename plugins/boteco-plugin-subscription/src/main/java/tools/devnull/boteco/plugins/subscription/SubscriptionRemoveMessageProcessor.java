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

package tools.devnull.boteco.plugins.subscription;

import tools.devnull.boteco.AlwaysActive;
import tools.devnull.boteco.event.SubscriptionManager;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;

@Command("unsubscribe")
@AlwaysActive
public class SubscriptionRemoveMessageProcessor implements MessageProcessor {

  private final SubscriptionManager subscriptionManager;

  public SubscriptionRemoveMessageProcessor(SubscriptionManager subscriptionManager) {
    this.subscriptionManager = subscriptionManager;
  }

  @Override
  public void process(IncomeMessage message) {
    SubscriptionParameters parameters = message.command().as(SubscriptionParameters.class);
    parameters.each(event -> {
      if (parameters.shouldRequestConfirmation()) {
        this.subscriptionManager
            .unsubscribe()
            .target(parameters.target())
            .ofChannel(parameters.channel())
            .withConfirmation()
            .fromEvent(event);
        message.reply("The subscription %s will be removed after confirmation!", event);
      } else {
        this.subscriptionManager
            .unsubscribe()
            .target(parameters.target())
            .ofChannel(parameters.channel())
            .fromEvent(event);
        message.reply("Subscription %s removed!", event);
      }
    });
  }

}
