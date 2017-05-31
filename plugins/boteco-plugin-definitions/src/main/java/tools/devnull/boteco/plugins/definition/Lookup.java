package tools.devnull.boteco.plugins.definition;

import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.plugins.definition.spi.Definition;
import tools.devnull.boteco.plugins.definition.spi.DefinitionProvider;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static tools.devnull.boteco.Predicates.id;

public class Lookup {

  private final ServiceRegistry registry;
  private final String provider;
  private final String term;

  public Lookup(ServiceRegistry registry, String provider, String term) {
    this.registry = registry;
    this.provider = provider;
    this.term = term;
  }

  public Lookup(ServiceRegistry registry, String term) {
    this.registry = registry;
    this.provider = null;
    this.term = term;
  }

  public String term() {
    return term;
  }

  public List<Definition> lookup() {
    List<Definition> result;
    if (provider == null) {
      result = registry.locate(DefinitionProvider.class).all()
          .stream()
          .map(p -> p.lookup(term))
          .collect(Collectors.toList());
    } else {
      DefinitionProvider definitionProvider = registry.locate(DefinitionProvider.class)
          .filter(id(provider))
          .one();
      result = Collections.singletonList(definitionProvider.lookup(term));
    }
    return result;
  }

}
