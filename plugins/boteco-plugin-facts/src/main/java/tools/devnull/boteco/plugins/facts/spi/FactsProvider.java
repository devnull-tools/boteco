package tools.devnull.boteco.plugins.facts.spi;

public interface FactsProvider {

  String id();

  Fact get();

}
