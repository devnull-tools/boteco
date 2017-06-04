package tools.devnull.boteco.providers.urbandictionary;

import com.google.gson.annotations.SerializedName;
import org.apache.http.client.utils.URIBuilder;
import tools.devnull.boteco.BotException;
import tools.devnull.boteco.client.rest.RestClient;
import tools.devnull.boteco.plugins.definition.spi.Definition;
import tools.devnull.boteco.plugins.definition.spi.DefinitionProvider;

import java.util.Comparator;
import java.util.List;

/**
 * A class that provides definitions for terms using the Urban Dictionary
 */
public class UrbanDictionaryDefinitionProvider implements DefinitionProvider {

  private final RestClient client;

  public UrbanDictionaryDefinitionProvider(RestClient client) {
    this.client = client;
  }

  @Override
  public Definition lookup(String term) {
    try {
      return client.get(new URIBuilder("http://api.urbandictionary.com")
          .setPath("/v0/define")
          .addParameter("term", term).build())
          .to(UrbanDictionaryResponse.class)
          .result().list.stream()
          .max(Comparator.comparingInt(_Definition::rate))
          .map(def -> new UrbanDictionaryDefinition(def.word, def.definition, def.permalink))
          .orElse(null);
    } catch (Exception e) {
      throw new BotException(e);
    }
  }

  public static class UrbanDictionaryResponse {

    private String[] tags;

    @SerializedName("result_type")
    private String resultType;

    private List<_Definition> list;

  }

  public static class _Definition {
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

  }

}
