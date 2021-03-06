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

package tools.devnull.boteco.channel.email;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import tools.devnull.boteco.Channel;
import tools.devnull.boteco.message.MessageDispatcher;

import javax.mail.internet.MimeUtility;

/**
 * A class that process incoming email messages
 */
public class EmailIncomeProcessor implements Processor {

  private final MessageDispatcher dispatcher;
  private final Channel channel;

  /**
   * Creates a new processor using the given parameters
   *
   * @param dispatcher  the component to dispatch the messages to be processed
   * @param channel     the channel implementation
   */
  public EmailIncomeProcessor(MessageDispatcher dispatcher, Channel channel) {
    this.dispatcher = dispatcher;
    this.channel = channel;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    String content = MimeUtility.decodeText(exchange.getIn().getHeader("Subject", String.class).trim());
    if (content.isEmpty()) {
      content = exchange.getIn().getBody(String.class).trim();
    }
    String sender = exchange.getIn().getHeader("From").toString();
    String target = exchange.getIn().getHeader("To").toString();

    if (!content.isEmpty()) {
      EmailSender emailSender = new EmailSender(sender);
      this.dispatcher.dispatch(new EmailMessage(channel, content, emailSender, target));
    }
  }

}
