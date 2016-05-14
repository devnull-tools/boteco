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

package tools.devnull.boteco.plugins.weather.searcher.yahoo;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.client.rest.RestClient;
import tools.devnull.boteco.plugins.weather.WeatherSearcher;

import java.net.URI;

public class YahooWeather implements WeatherSearcher {

  private static final Logger logger = LoggerFactory.getLogger(YahooWeather.class);

  private final RestClient client;

  public YahooWeather(RestClient client) {
    this.client = client;
  }

  @Override
  public WeatherResults search(String query) {
    try {
      URI uri = new URIBuilder()
          .setScheme("https")
          .setHost("query.yahooapis.com")
          .setPath("/v1/public/yql")
          .addParameter("format", "json")
          .addParameter("q", "select item from weather.forecast where woeid in " +
              "(select woeid from geo.places(1) where text=\"" +
              query + "\") and u=\"c\"")
          .build();
      WeatherResults results = client.get(uri).to(WeatherResults.class).result();
      return results.hasResult() ? results : null;
    } catch (Exception e) {
      logger.error("Error while querying weather", e);
    }
    return null;
  }

}
