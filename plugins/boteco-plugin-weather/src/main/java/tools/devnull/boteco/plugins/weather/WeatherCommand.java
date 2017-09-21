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

import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.message.MessageProcessingException;
import tools.devnull.boteco.plugins.weather.spi.Weather;
import tools.devnull.boteco.plugins.weather.spi.WeatherSearcher;
import tools.devnull.trugger.Optional;

public class WeatherCommand {

  private final ServiceRegistry registry;
  private final String provider;
  private final String query;

  public WeatherCommand(ServiceRegistry registry, String provider, String query) {
    this.registry = registry;
    this.provider = provider;
    this.query = query;
  }

  public WeatherCommand(ServiceRegistry registry, String query) {
    this.registry = registry;
    this.provider = null;
    this.query = query;
  }

  public Optional<Weather> search() {
    return searcher().search(query);
  }

  private WeatherSearcher searcher() {
    if (provider != null) {
      return registry.providerOf(WeatherSearcher.class, provider)
          .orElseThrow(() -> new MessageProcessingException(String.format("Provider %s not found", provider)))
          .get();
    } else {
      return registry.providerOf(WeatherSearcher.class)
          .orElseThrow(() -> new MessageProcessingException("No provider for weather forecast"))
          .get();
    }
  }

}
