package tools.devnull.boteco.providers.urbandictionary;

import tools.devnull.boteco.plugins.definition.spi.Definition;

/**
 * Implementation of a definition from the Urban Dictionary
 */
public class UrbanDictionaryDefinition implements Definition {

  private final String source = "Urban Dictionary";
  private final String term;
  private final String description;
  private final String url;

  public UrbanDictionaryDefinition(String term, String description, String url) {
    this.term = term;
    this.description = description;
    this.url = url;
  }

  @Override
  public String term() {
    return this.term;
  }

  @Override
  public String source() {
    return this.source;
  }

  @Override
  public String description() {
    return this.description;
  }

  @Override
  public String url() {
    return this.url;
  }

}
