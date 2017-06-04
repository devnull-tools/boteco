package tools.devnull.boteco.plugins.facts;

import tools.devnull.boteco.Name;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;
import tools.devnull.boteco.plugins.facts.spi.FactsProvider;

import java.util.List;
import java.util.stream.Collectors;

@Name("facts")
@Command("facts")
public class FactsMessageProcessor implements MessageProcessor {

  private final List<FactsProvider> providers;

  public FactsMessageProcessor(List<FactsProvider> providers) {
    this.providers = providers;
  }

  @Override
  public void process(IncomeMessage message) {
    message.reply(providers.stream()
        .map(FactsProvider::id)
        .collect(Collectors.joining("\n"))
    );
  }

}
