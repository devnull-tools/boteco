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

package tools.devnull.boteco.plugins.stocks;

import tools.devnull.boteco.BotException;
import tools.devnull.boteco.client.rest.RestClient;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Function;

@Command("stock")
public class StocksMessageProcessor implements MessageProcessor {

  private final RestClient restClient;
  private final Properties configuration;

  public StocksMessageProcessor(RestClient restClient, Properties configuration) {
    this.restClient = restClient;
    this.configuration = configuration;
  }

  @Override
  public void process(IncomeMessage message) {
    String arg = message.command().as(String.class);
    String query;
    if (!arg.contains(":") && configuration.containsKey("query.defaults.exchange")) {
      query = configuration.getProperty("query.defaults.exchange") + ":" + arg;
    } else {
      query = arg;
    }
    String url = "https://finance.google.com/finance/info?client=ig&q=" + query;
    try {
      restClient.get(url)
          .extract(json())
          .to(StockResult.class)
          .and(stock -> stock.reply(message))
          .orElse(() -> message.reply("I didn't find results for [a]%s[/a]", query
          ));
    } catch (IOException e) {
      throw new BotException(e.getMessage(), e);
    }
  }

  // extracts the json portion of the result
  private Function<String, String> json() {
    return body ->
        (body.contains("{") && body.contains("}")) ?
            body.substring(body.indexOf("{"), body.lastIndexOf("}") + 1) :
            body;

  }

}
