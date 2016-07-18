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

import tools.devnull.boteco.event.SubscriptionManager;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;

import java.util.List;

import static tools.devnull.boteco.Predicates.command;
import static tools.devnull.boteco.message.MessageChecker.check;

public class SubscriptionManagerMessageProcessor implements MessageProcessor {

  private final SubscriptionManager subscriptionManager;

  public SubscriptionManagerMessageProcessor(SubscriptionManager subscriptionManager) {
    this.subscriptionManager = subscriptionManager;
  }

  @Override
  public String id() {
    return "subscription-manager";
  }

  @Override
  public boolean canProcess(IncomeMessage message) {
    return check(message).accept(command("subscription").withArgs());
  }

  @Override
  public void process(IncomeMessage message) {
    List<String> args = message.command().args();
    String operation = args.get(0);
    switch (operation) {
      case "add":
        if (args.size() == 4) {
          this.subscriptionManager.subscribe()
              .target(args.get(2))
              .ofChannel(args.get(1))
              .withConfirmation()
              .toEvent(args.get(3));
        } else {
          String target = message.isGroup() ? message.target() : message.sender().id();
          this.subscriptionManager.subscribe()
              .target(target)
              .ofChannel(message.channel().id())
              .toEvent(args.get(1));
          message.reply("Subscription added!");
        }
        break;
      case "remove":
        if (args.size() == 4) {
          this.subscriptionManager.unsubscribe()
              .target(args.get(2))
              .ofChannel(args.get(1))
              .withConfirmation()
              .fromEvent(args.get(3));
        } else {
          String target = message.isGroup() ? message.target() : message.sender().id();
          this.subscriptionManager.unsubscribe()
              .target(target)
              .ofChannel(message.channel().id())
              .fromEvent(args.get(1));
          message.reply("Subscription removed!");
        }
      case "confirm":
        this.subscriptionManager.confirm(args.get(1));
        break;
    }
  }

}
