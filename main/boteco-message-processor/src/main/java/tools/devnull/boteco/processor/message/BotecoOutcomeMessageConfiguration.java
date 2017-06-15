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

package tools.devnull.boteco.processor.message;

import tools.devnull.boteco.client.jms.JmsClient;
import tools.devnull.boteco.message.OutcomeMessage;
import tools.devnull.boteco.message.OutcomeMessageConfiguration;
import tools.devnull.boteco.Sendable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static tools.devnull.boteco.Destination.queue;

/**
 * A default implementation to a message builder based on a queue format to send
 * the messages.
 */
public class BotecoOutcomeMessageConfiguration implements OutcomeMessageConfiguration {

  private final JmsClient client;
  private final String queueFormat;
  private final Map<String, Object> headers;
  private String target;
  private String replyId;
  private Sendable object;

  /**
   * Creates a new message builder based on the given parameters.
   * <p>
   * Since this class is based on a naming convention, the queue format
   * must have a "%s" to represent the channel name in the queue's name.
   * <p>
   * The given object will be used to fill up the message properties based on
   * the {@link Sendable} interface. You can override then by specific
   * calling the methods to build the outcome message.
   *
   * @param client      the jms client to send the messages
   * @param queueFormat the queue format to generate the queue name
   * @param object      the object to send
   */
  public BotecoOutcomeMessageConfiguration(JmsClient client, String queueFormat, Sendable object) {
    this.client = client;
    this.queueFormat = queueFormat;
    this.headers = new HashMap<>();
    this.object = object;
  }

  @Override
  public OutcomeMessageConfiguration to(String target) {
    this.target = target;
    return this;
  }

  @Override
  public OutcomeMessageConfiguration with(String headerName, Object headerValue) {
    this.headers.put(headerName, headerValue);
    return this;
  }

  @Override
  public OutcomeMessageConfiguration replyingTo(String id) {
    this.replyId = id;
    return this;
  }

  @Override
  public void through(String channel) {
    client.send(new OutcomeMessage(object, target, headers, replyId))
        .expiringIn(5, TimeUnit.MINUTES)
        .to(queue(String.format(queueFormat, channel)));
  }

}
