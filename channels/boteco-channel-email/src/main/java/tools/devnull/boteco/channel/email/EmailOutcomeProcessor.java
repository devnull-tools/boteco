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
import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.message.FormatExpressionParser;
import tools.devnull.boteco.message.OutcomeMessage;

public class EmailOutcomeProcessor implements Processor {

  private final FormatExpressionParser parser;
  private final ContentFormatter contentFormatter;

  public EmailOutcomeProcessor(FormatExpressionParser parser) {
    this.parser = parser;
    this.contentFormatter = new EmailContentFormatter();
  }


  @Override
  public void process(Exchange exchange) throws Exception {
    OutcomeMessage message = exchange.getIn().getBody(OutcomeMessage.class);
    if (message != null) {
      exchange.getOut().setHeader("To", message.getTarget());
      message.eachHeader(entry -> exchange.getOut().setHeader(entry.getKey(), entry.getValue()));
      exchange.getOut().setBody(parser.parse(contentFormatter, message.getContent()).replaceAll("\\n", "<br>"));
    }
  }

}
