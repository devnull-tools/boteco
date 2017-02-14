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

package tools.devnull.boteco.channel.pushover;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.client.rest.RestClient;
import tools.devnull.boteco.message.FormatExpressionParser;
import tools.devnull.boteco.message.OutcomeMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A processor for outcome pushover messages
 */
public class PushoverOutcomeProcessor implements Processor {

  private final FormatExpressionParser parser;
  private final ContentFormatter formatter;
  private final RestClient client;
  private final String token;

  /**
   * Creates a new processor based on the given parameters
   *
   * @param parser    the parser for processing the format blocks
   * @param formatter the formatter to format the parsed blocks
   * @param client    the rest client for sending the requests to the Pushover API
   * @param token     the pushover token to use
   */
  public PushoverOutcomeProcessor(FormatExpressionParser parser,
                                  ContentFormatter formatter,
                                  RestClient client,
                                  String token) {
    this.parser = parser;
    this.formatter = formatter;
    this.client = client;
    this.token = token;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    OutcomeMessage out = exchange.getIn().getBody(OutcomeMessage.class);
    Map<String, String> body = new HashMap<>();

    body.put("token", token);
    body.put("user", out.getTarget());
    body.put("message", parser.parse(formatter, out.getContent()));
    out.ifTitle(title -> body.put("title", title));
    out.ifUrl(url -> body.put("url", url));

    if (out.isHighPriority()) {
      body.put("priority", "1");
    } else if (out.isLowPriority()) {
      body.put("priority", "-1");
    }

    client.post("https://api.pushover.net/1/messages.json")
        .with(body).asFormUrlEncoded()
        .retryOnConnectionError(5)
        .waitAfterRetry(1, TimeUnit.SECONDS)
        .execute();
  }

}
