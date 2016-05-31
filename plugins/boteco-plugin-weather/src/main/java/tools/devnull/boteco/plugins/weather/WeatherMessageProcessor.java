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
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;

import java.util.List;

import static tools.devnull.boteco.Predicates.command;
import static tools.devnull.boteco.message.MessageChecker.check;

public class WeatherMessageProcessor implements MessageProcessor {

  private static final Logger logger = LoggerFactory.getLogger(WeatherMessageProcessor.class);

  private final List<WeatherSearcher> searchers;

  public WeatherMessageProcessor(List<WeatherSearcher> searchers) {
    this.searchers = searchers;
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
      Weather weather = search(message.command().arg());
      if (weather != null) {
        message.sendBack(buildResponse(weather));
      } else {
        message.sendBack("[e]Could not find any weather for " + message.command().arg() + "[/e]");
      }
    } catch (Exception e) {
      logger.error("Error while fetching weather", e);
    }
  }

  private Weather search(String query) {
    Weather weather;
    for (WeatherSearcher searcher : searchers) {
      weather = searcher.search(query);
      if (weather != null) {
        return weather;
      }
    }
    return null;
  }

  private String buildResponse(Weather weather) {
    StringBuilder response = new StringBuilder();
    if (weather.text() != null) {
      response.append("[a]").append(weather.text()).append("[/a]");
    }
    if (weather.condition() != null) {
      response.append(": [aa]").append(weather.condition()).append("[/aa]");
    }
    if (weather.temperature() != null) {
      response.append(" - [v]")
          .append(String.valueOf((int) weather.temperature().celsius())).append("\u00BAC").append("[/v]")
          .append(" / [v]").append(String.valueOf((int) weather.temperature().fahrenheits())).append("\u00BAF")
          .append("[/v]");
    }
    return response.toString();
  }

}
