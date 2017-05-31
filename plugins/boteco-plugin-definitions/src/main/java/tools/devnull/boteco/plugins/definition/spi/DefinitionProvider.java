package tools.devnull.boteco.plugins.definition.spi;

/**
 * Interface that defines a provider for a definition. Examples may include
 * the Urban Dictionary, Wikipedia, etc.
 */
public interface DefinitionProvider {

  /**
   * Retrieve information regarding the given term. The information may be related
   * to the term
   * @param term the term to search
   * @return the definition found by this provider
   */
  Definition lookup(String term);

}
