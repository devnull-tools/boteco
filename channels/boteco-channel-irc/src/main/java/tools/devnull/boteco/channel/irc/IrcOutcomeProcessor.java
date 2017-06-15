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

package tools.devnull.boteco.channel.irc;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.irc.IrcConstants;
import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.message.FormatExpressionParser;
import tools.devnull.boteco.message.OutcomeMessage;

/**
 * A processor to dispatch messages from the bot to the IRC
 */
public class IrcOutcomeProcessor implements Processor {

  private final FormatExpressionParser parser;
  private final ContentFormatter contentFormatter;

  /**
   * Creates a new processor using the given parameters
   *
   * @param parser           the expression parser to use
   * @param contentFormatter the content formatter to use
   */
  public IrcOutcomeProcessor(FormatExpressionParser parser, ContentFormatter contentFormatter) {
    this.parser = parser;
    this.contentFormatter = contentFormatter;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    OutcomeMessage out = exchange.getIn().getBody(OutcomeMessage.class);
    if (out != null) {
      exchange.getOut().setHeader(IrcConstants.IRC_TARGET, out.target());
      exchange.getOut().setHeader(IrcConstants.IRC_MESSAGE_TYPE, "PRIVMSG");

      StringBuilder message = new StringBuilder();

      out.ifReply(id -> message.append(id).append(", "));
      out.ifTitle(title -> message.append(title).append(": "));

      message.append(out.message());

      out.ifUrl(url -> message.append(" <").append(url).append(">"));

      exchange.getOut().setBody(parser.parse(contentFormatter, message.toString()));
    }
  }

}
