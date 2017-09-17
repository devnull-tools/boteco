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

package tools.devnull.boteco.providers.chucknorris;

import tools.devnull.boteco.BotException;
import tools.devnull.boteco.client.rest.RestClient;
import tools.devnull.boteco.plugins.facts.spi.Fact;
import tools.devnull.boteco.provider.Provider;

import java.io.IOException;

public class ChuckNorrisFactsProvider implements Provider<Fact> {

  private final RestClient rest;

  public ChuckNorrisFactsProvider(RestClient restClient) {
    this.rest = restClient;
  }

  @Override
  public String id() {
    return "chucknorris";
  }

  @Override
  public Fact get() {
    try {
      return rest.get("https://api.chucknorris.io/jokes/random")
          .to(ChuckNorrisFact.class)
          .value();
    } catch (IOException e) {
      throw new BotException(e);
    }
  }

}
