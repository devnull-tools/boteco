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

package tools.devnull.boteco.plugins.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;

import static tools.devnull.boteco.Predicates.command;
import static tools.devnull.boteco.message.MessageChecker.check;

public class WeatherMessageProcessor implements MessageProcessor {

  private static final Logger logger = LoggerFactory.getLogger(WeatherMessageProcessor.class);

  private final WeatherSearcher searcher;

  public WeatherMessageProcessor(WeatherSearcher searcher) {
    this.searcher = searcher;
  }

  @Override
  public String id() {
    return "weather";
  }

  @Override
  public boolean canProcess(IncomeMessage message) {
    return check(message).accept(command("weather").withArgs());
  }

  @Override
  public void process(IncomeMessage message) {
    try {
      Weather weather = searcher.search(message.command().arg());
      ContentFormatter formatter = message.channel().formatter();
      if (weather != null) {
        message.reply(String.format("%s: %s - %s / %s",
            formatter.accent(weather.text()),
            formatter.alternativeAccent(weather.condition()),
            formatter.value(String.valueOf((int) weather.temperature().celsius()) + "\u00BAC"),
            formatter.value(String.valueOf((int) weather.temperature().fahrenheits()) + "\u00BAF")));
      } else {
        message.reply(formatter.error("Could not find any weather for " + message.command().arg()));
      }
    } catch (Exception e) {
      logger.error("Error while fetching weather", e);
    }
  }

}
