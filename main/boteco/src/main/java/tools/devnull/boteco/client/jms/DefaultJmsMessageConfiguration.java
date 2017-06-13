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

package tools.devnull.boteco.client.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.BotException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * The default implementation of a JmsMessageConfiguration
 */
public class DefaultJmsMessageConfiguration implements JmsMessageConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(DefaultJmsMessageConfiguration.class);

  private final ConnectionFactory connectionFactory;
  private final Serializable object;
  private final Long expirationTime;
  private final Function<Connection, Session> createFunction;

  public DefaultJmsMessageConfiguration(ConnectionFactory connectionFactory, Serializable object) {
    this.connectionFactory = connectionFactory;
    this.object = object;
    this.expirationTime = null;
    this.createFunction = defaultCreateFunction();
  }

  public DefaultJmsMessageConfiguration(ConnectionFactory connectionFactory,
                                        Serializable object,
                                        Long expirationTime,
                                        Function<Connection, Session> createFunction) {
    this.connectionFactory = connectionFactory;
    this.object = object;
    this.expirationTime = expirationTime;
    this.createFunction = createFunction;
  }

  @Override
  public JmsMessageConfiguration withSession(Function<Connection, Session> creationFunction) {
    return new DefaultJmsMessageConfiguration(this.connectionFactory, this.object, this.expirationTime, creationFunction);
  }

  @Override
  public JmsMessageConfiguration expiringIn(long amount, TimeUnit unit) {
    return new DefaultJmsMessageConfiguration(this.connectionFactory, this.object, unit.toMillis(amount), this.createFunction);
  }

  @Override
  public void to(JmsDestination target) {
    try {
      Connection connection = connectionFactory.createConnection();
      connection.start();

      Session session = createFunction.apply(connection);
      Destination destination = target.createDestination(session);

      MessageProducer producer = session.createProducer(destination);
      producer.setDeliveryMode(DeliveryMode.PERSISTENT);
      if (this.expirationTime != null) {
        producer.setTimeToLive(this.expirationTime);
      }

      ObjectMessage message = session.createObjectMessage(object);
      producer.send(message);

      session.close();
      connection.close();
    } catch (JMSException e) {
      logger.error("Error while sending object to AMQ destination", e);
    }
  }

  private Function<Connection, Session> defaultCreateFunction() {
    return connection -> {
      try {
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      } catch (JMSException e) {
        logger.error("Error while creating session: " + e.getMessage(), e);
        throw new BotException(e);
      }
    };
  }

}
