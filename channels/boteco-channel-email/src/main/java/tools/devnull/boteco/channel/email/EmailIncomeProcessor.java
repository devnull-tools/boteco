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
import tools.devnull.boteco.ServiceLocator;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageDispatcher;

import javax.mail.internet.MimeUtility;

public class EmailIncomeProcessor implements Processor {

  private final MessageDispatcher dispatcher;
  private final ServiceLocator serviceLocator;

  public EmailIncomeProcessor(MessageDispatcher dispatcher, ServiceLocator serviceLocator) {
    this.dispatcher = dispatcher;
    this.serviceLocator = serviceLocator;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    String content = MimeUtility.decodeText(exchange.getIn().getHeader("Subject").toString().trim());
    if (content.isEmpty()) {
      content = exchange.getIn().getBody(String.class).trim();
    }
    String sender = exchange.getIn().getHeader("From").toString();
    String target = exchange.getIn().getHeader("To").toString();

    if (!content.isEmpty()) {
      IncomeMessage message = new EmailIncomeMessage(this.serviceLocator, content, sender, target);
      this.dispatcher.dispatch(message);
    }
  }

}