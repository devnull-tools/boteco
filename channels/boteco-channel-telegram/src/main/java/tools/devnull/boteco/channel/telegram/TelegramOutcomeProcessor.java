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
import tools.devnull.boteco.BotException;
import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.client.rest.RestClient;
import tools.devnull.boteco.message.FormatExpressionParser;
import tools.devnull.boteco.message.OutcomeMessage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A processor that receives an outcome telegram message and sends it as a bot message.
 */
public class TelegramOutcomeProcessor implements Processor {

  private static final Integer MAX_CHARS = 4000;

  private final FormatExpressionParser parser;
  private final ContentFormatter markdownContentFormatter;
  private final ContentFormatter defaultContentFormatter;
  private final RestClient client;
  private final String token;

  /**
   * Creates a new processor based on the given parameters
   *
   * @param parser                   the parser for formatting purposes
   * @param markdownContentFormatter the content formatter for markdown format
   * @param defaultContentFormatter  the content formatter for non markdown format
   * @param client                   the rest client to use
   * @param token                    the bot token
   */
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
    if (!(message == null || message.getContent() == null || message.getContent().isEmpty())) {
      Map<String, String> body = new HashMap<>();
      String content = message.getContent();
      message.eachMetadata(header -> body.put(header.getKey(), String.valueOf(header.getValue())));
      body.put("chat_id", message.getTarget());
      checkAndSend(body, content);
    }
  }

  private void checkAndSend(Map<String, String> body, String content) {
    String remaining = null;
    if (content.length() > MAX_CHARS) {
      StringBuilder temp = new StringBuilder(content);
      int end = content.length();
      while (end > MAX_CHARS) {
        end = temp.lastIndexOf("\n");
        if (end == -1) {
          end = temp.lastIndexOf(" ");
        }
        if (end == -1) {
          end = MAX_CHARS;
        }
        temp.delete(end, temp.length() - 1);
      }
      remaining = content.substring(end);
      content = temp.toString() + " ...";
    }
    ContentFormatter formatter;
    // avoids conflicts with the markdown syntax
    if (canUseMarkdown(content)) {
      formatter = markdownContentFormatter;
      body.put("parse_mode", "Markdown");
    } else {
      formatter = defaultContentFormatter;
    }
    content = parser.parse(formatter, content);
    body.put("text", content);
    sendMessage(body);
    if (remaining != null) {
      checkAndSend(body, remaining);
    }
  }

  private void sendMessage(Map<String, String> body) {
    try {
      client.post("https://api.telegram.org/bot" + token + "/sendMessage")
          .with(body).asJson()
          .on(429, response -> sendMessage(body)) // limit error
          .execute();
    } catch (IOException e) {
      throw new BotException(e);
    }
  }

  private boolean canUseMarkdown(String content) {
    return Arrays.stream(new String[]{"*", "_", "`", "[", "]"})
        .anyMatch(content::contains);
  }

}
