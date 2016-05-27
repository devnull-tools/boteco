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

package tools.devnull.boteco.process;

import tools.devnull.boteco.Rule;
import tools.devnull.boteco.ServiceLocator;
import tools.devnull.boteco.client.jms.JmsClient;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageDispatcher;

import java.util.List;

import static tools.devnull.boteco.client.jms.Destinations.queue;

public class BotecoMessageDispatcher implements MessageDispatcher {

  private final JmsClient client;
  private final ServiceLocator serviceLocator;
  private final String queueName;

  public BotecoMessageDispatcher(JmsClient client, ServiceLocator serviceLocator, String queueName) {
    this.client = client;
    this.serviceLocator = serviceLocator;
    this.queueName = queueName;
  }

  @Override
  public void dispatch(IncomeMessage incomeMessage) {
    List<Rule> rules = serviceLocator.locateAll(Rule.class,
        "(|(channel=all)(channel=%s))", incomeMessage.channel().id());
    if (rules.isEmpty() || rules.stream().allMatch(rule -> rule.accept(incomeMessage))) {
      client.send(incomeMessage).to(queue(queueName));
    }
  }

}
