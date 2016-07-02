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

package tools.devnull.boteco.channel.telegram;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.http.client.utils.URIBuilder;
import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.client.rest.RestClient;
import tools.devnull.boteco.message.FormatExpressionParser;
import tools.devnull.boteco.message.OutcomeMessage;

import java.net.URI;

/**
 * A processor that receives an outcome telegram message and sends it as a bot message.
 */
public class TelegramOutcomeProcessor implements Processor {

  private final FormatExpressionParser parser;
  private final ContentFormatter markdownContentFormatter;
  private final ContentFormatter defaultContentFormatter;
  private final RestClient client;
  private final String token;

  public TelegramOutcomeProcessor(FormatExpressionParser parser,
                                  ContentFormatter markdownContentFormatter,
                                  ContentFormatter defaultContentFormatter,
                                  RestClient client,
                                  String token) {
    this.parser = parser;
    this.markdownContentFormatter = markdownContentFormatter;
    this.defaultContentFormatter = defaultContentFormatter;
    this.client = client;
    this.token = token;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    OutcomeMessage message = exchange.getIn().getBody(OutcomeMessage.class);
    if (message != null) {
      URIBuilder builder = new URIBuilder("https://api.telegram.org")
          .setPath("/bot" + token + "/sendMessage");
      // avoids conflicts with the markdown syntax
      String content = message.getContent();
      ContentFormatter formatter;
      if (!(content.contains("*") || content.contains("_") || content.contains("`"))) {
        formatter = markdownContentFormatter;
        builder.addParameter("parse_mode", "Markdown");
      } else {
        formatter = defaultContentFormatter;
      }
      message.eachHeader(header -> builder.addParameter(header.getKey(), String.valueOf(header.getValue())));
      content = parser.parse(formatter, content);
      URI uri = builder
          .addParameter("chat_id", message.getTarget())
          .addParameter("text", content)
          .build();
      client.post(uri).execute();
    }
  }
}
