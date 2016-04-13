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

package tools.devnull.boteco.domain;

import tools.devnull.boteco.domain.OutcomeMessage;
import tools.devnull.boteco.domain.OutcomeMessageBuilder;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;

public class BotecoOutcomeMessageBuilder implements OutcomeMessageBuilder {

  private final QueueConnectionFactory connectionFactory;

  private String content;
  private String target;

  public BotecoOutcomeMessageBuilder(QueueConnectionFactory connectionFactory) {
    this.connectionFactory = connectionFactory;
  }

  @Override
  public OutcomeMessageBuilder content(String content) {
    this.content = content;
    return this;
  }

  @Override
  public OutcomeMessageBuilder to(String target) {
    this.target = target;
    return this;
  }

  @Override
  public void throught(String channel) {
    try {
      Connection connection = connectionFactory.createConnection();
      connection.start();

      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      Destination destination = session.createQueue("boteco.message." + channel);

      MessageProducer producer = session.createProducer(destination);
      producer.setDeliveryMode(DeliveryMode.PERSISTENT);

      ObjectMessage message = session.createObjectMessage(new OutcomeMessage(target, content));
      producer.send(message);

      session.close();
      connection.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
