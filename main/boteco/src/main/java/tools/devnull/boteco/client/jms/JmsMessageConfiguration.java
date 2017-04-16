package tools.devnull.boteco.client.jms;

import java.util.concurrent.TimeUnit;

/**
 * Interface that configures a message for sending.
 */
public interface JmsMessageConfiguration {

  /**
   * Sets the expiration time for this message.
   *
   * @param amount the amount of time
   * @param unit   the time unit
   * @return an instance of this class
   */
  JmsMessageConfiguration expiringIn(long amount, TimeUnit unit);

  /**
   * Sends the message to the given destination.
   *
   * @param destination the destination to send the message
   */
  void to(JmsDestination destination);

}
