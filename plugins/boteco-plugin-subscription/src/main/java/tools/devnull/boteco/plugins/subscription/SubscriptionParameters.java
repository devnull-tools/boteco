/*
 * The MIT License
 *
 * Copyright (c) 2016-2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

import tools.devnull.boteco.message.IncomeMessage;

import java.util.Arrays;
import java.util.function.Consumer;

public class SubscriptionParameters {

  private final String[] events;
  private final String channel;
  private final String target;
  private final boolean requestConfirmation;

  public SubscriptionParameters(IncomeMessage message, String[] events) {
    if (message.user() != null && !message.isGroup()) {
      this.target = message.user().id();
      this.channel = "user";
    } else {
      this.target = message.isGroup() ? message.target() : message.sender().id();
      this.channel = message.channel().id();
    }
    this.events = events;
    this.requestConfirmation = false;
  }

  public SubscriptionParameters(String[] events, String channel, String target) {
    this.events = events;
    this.target = target;
    this.channel = channel;
    this.requestConfirmation = true;
  }

  public void each(Consumer<String> consumer) {
    Arrays.stream(events).forEach(consumer);
  }

  public String channel() {
    return channel;
  }

  public String target() {
    return target;
  }

  public boolean shouldRequestConfirmation() {
    return requestConfirmation;
  }

}
