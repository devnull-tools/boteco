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

import tools.devnull.boteco.Builder;
import tools.devnull.boteco.client.jms.JmsClient;
import tools.devnull.boteco.message.MessageSender;
import tools.devnull.boteco.message.OutcomeMessageConfiguration;
import tools.devnull.boteco.Sendable;

/**
 * The default implementation of a message sender component based on
 * a jms client to send the messages.
 */
public class BotecoMessageSender implements MessageSender {

  private static final long serialVersionUID = 8229143816118073058L;

  private final JmsClient client;
  private final String queueFormat;

  /**
   * Creates a new message sender based on the given parameters
   * <p>
   * Since this class is based on a naming convention, the queue format
   * must have a "%s" to represent the channel name in the queue's name.
   *
   * @param client      the jms client to send the messages
   * @param queueFormat the format to create queue names
   */
  public BotecoMessageSender(JmsClient client, String queueFormat) {
    this.client = client;
    this.queueFormat = queueFormat;
  }

  @Override
  public OutcomeMessageConfiguration send(Sendable object) {
    return new BotecoOutcomeMessageConfiguration(client, queueFormat, object);
  }

  @Override
  public OutcomeMessageConfiguration send(Builder<Sendable> builder) {
    return send(builder.build());
  }

}
