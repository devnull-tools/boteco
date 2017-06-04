package tools.devnull.boteco.plugins.facts;

import tools.devnull.boteco.DomainException;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.plugins.facts.spi.Fact;
import tools.devnull.boteco.plugins.facts.spi.FactsProvider;

import static tools.devnull.boteco.Predicates.id;

public class FactRequest {

  private final ServiceRegistry registry;
  private final String factName;

  public FactRequest(ServiceRegistry registry, String factName) {
    this.registry = registry;
    this.factName = factName;
  }

  public Fact fetch() {
    return registry.locate(FactsProvider.class)
        .filter(id(factName))
        .orElseThrow(() -> new DomainException("No facts provider for " + factName))
        .get();
  }

}
