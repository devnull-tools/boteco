package tools.devnull.boteco.client.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * The default implementation of a JmsMessageConfiguration
 */
public class DefaultJmsMessageConfiguration implements JmsMessageConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(DefaultJmsMessageConfiguration.class);

  private final ConnectionFactory connectionFactory;
  private final Serializable object;
  private final Long expirationTime;

  public DefaultJmsMessageConfiguration(ConnectionFactory connectionFactory, Serializable object) {
    this.connectionFactory = connectionFactory;
    this.object = object;
    this.expirationTime = null;
  }

  public DefaultJmsMessageConfiguration(ConnectionFactory connectionFactory,
                                        Serializable object,
                                        Long expirationTime) {
    this.connectionFactory = connectionFactory;
    this.object = object;
    this.expirationTime = expirationTime;
  }

  @Override
  public JmsMessageConfiguration expiringIn(long amount, TimeUnit unit) {
    return new DefaultJmsMessageConfiguration(this.connectionFactory, this.object, unit.toMillis(amount));
  }

  @Override
  public void to(JmsDestination target) {
    try {
      Connection connection = connectionFactory.createConnection();
      connection.start();

      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
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
}
