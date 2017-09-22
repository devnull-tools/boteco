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

package tools.devnull.boteco.providers.urbandictionary;

import com.google.gson.annotations.SerializedName;
import org.apache.http.client.utils.URIBuilder;
import tools.devnull.boteco.BotException;
import tools.devnull.boteco.client.rest.RestClient;
import tools.devnull.boteco.plugins.definition.spi.Definition;
import tools.devnull.boteco.plugins.definition.spi.DefinitionLookup;
import tools.devnull.trugger.Optional;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;

/**
 * A class that provides definitions for terms using the Urban Dictionary
 */
public class UrbanDictionaryDefinitionLookup implements DefinitionLookup {

  private final RestClient client;

  public UrbanDictionaryDefinitionLookup(RestClient client) {
    this.client = client;
  }

  @Override
  public Optional<Definition> lookup(String term) {
    try {
      return Optional.of(client.get(new URIBuilder("http://api.urbandictionary.com")
          .setPath("/v0/define")
          .addParameter("term", term).build())
          .to(UrbanDictionaryResponse.class)
          .value().list.stream()
          .max(Comparator.comparingInt(_Definition::rate))
          .orElse(null));
    } catch (IOException | URISyntaxException e) {
      throw new BotException(e);
    }
  }

  public static class UrbanDictionaryResponse {

    private String[] tags;

    @SerializedName("result_type")
    private String resultType;

    private List<_Definition> list;

  }

  public static class _Definition implements Definition {
    @SerializedName("defid")
    private int id;

    @SerializedName("word")
    private String word;

    @SerializedName("author")
    private String author;

    @SerializedName("permalink")
    private String permalink;

    @SerializedName("definition")
    private String definition;

    @SerializedName("example")
    private String example;

    @SerializedName("thumbs_up")
    private int thumbsUp;

    @SerializedName("thumbs_down")
    private int thumbsDown;

    @SerializedName("current_vote")
    private String currentVote;

    int rate() {
      return thumbsUp - thumbsDown;
    }

    @Override
    public String term() {
      return word;
    }

    @Override
    public String source() {
      return "Urban Dictionary";
    }

    @Override
    public String description() {
      return definition;
    }

    @Override
    public String url() {
      return permalink;
    }
  }

}
