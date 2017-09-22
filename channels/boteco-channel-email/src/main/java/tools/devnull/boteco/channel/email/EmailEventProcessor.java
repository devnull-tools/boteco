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
import tools.devnull.boteco.event.EventBus;

import javax.mail.internet.MimeUtility;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tools.devnull.boteco.message.MessageBuilder.message;

/**
 * A processor for events arriving through email messages
 */
public class EmailEventProcessor implements Processor {

  private final EventBus eventBus;
  private final Pattern pattern;

  public EmailEventProcessor(EventBus eventBus, String pattern) {
    this.eventBus = eventBus;
    this.pattern = Pattern.compile(pattern);
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    String subject = MimeUtility.decodeText(exchange.getIn().getHeader("Subject", String.class).trim());
    String content = exchange.getIn().getBody(String.class).trim();
    Matcher matcher = pattern.matcher(subject);
    if (matcher.find()) {
      String id = matcher.group("id");
      String title = matcher.group("title");
      eventBus.broadcast(message(content)
          .withTitle(title))
          .as(id);
    }
  }

}
