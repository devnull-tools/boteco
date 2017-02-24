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

package tools.devnull.boteco.processor.message;

import tools.devnull.boteco.Rule;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.client.jms.JmsClient;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageDispatcher;

import java.util.List;

import static tools.devnull.boteco.Destination.queue;
import static tools.devnull.boteco.Predicates.eq;
import static tools.devnull.boteco.Predicates.serviceProperty;

/**
 * A component responsible for dispatching messages for being processed by
 * delivering them to a jms queue.
 * <p>
 * The destination queue is based on a given name plus a "." and the channel
 * (e.g. "boteco.process.irc").
 * <p>
 * The purpose of this class is also to apply {@link Rule rules} so only allowed
 * messages will be dispatched to their respective queues.
 */
public class BotecoMessageDispatcher implements MessageDispatcher {

  private final JmsClient client;
  private final ServiceRegistry serviceRegistry;
  private final String queueName;

  /**
   * Creates a new message dispatcher based on the given parameters
   *
   * @param client          the jms client for sending the messages to queues
   * @param serviceRegistry the service registry to lookup rules
   * @param queueName       the queue name to use
   */
  public BotecoMessageDispatcher(JmsClient client, ServiceRegistry serviceRegistry, String queueName) {
    this.client = client;
    this.serviceRegistry = serviceRegistry;
    this.queueName = queueName;
  }

  @Override
  public void dispatch(IncomeMessage incomeMessage) {
    List<Rule> rules = serviceRegistry.locate(Rule.class)
        .filter(serviceProperty("channel", eq("all").or(eq(incomeMessage.channel().id()))))
        .all();
    if (rules.isEmpty() || rules.stream().allMatch(rule -> rule.accept(incomeMessage))) {
      client.send(incomeMessage).to(queue(queueName + "." + incomeMessage.channel().id()));
    }
  }

}
