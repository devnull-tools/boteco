package tools.devnull.boteco.plugins.facts;

import tools.devnull.boteco.Name;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;
import tools.devnull.boteco.plugins.facts.spi.Fact;

@Name("facts")
@Command("fact")
public class FactMessageProcessor implements MessageProcessor {

  @Override
  public void process(IncomeMessage message) {
    Fact fact = message.command().as(FactRequest.class).fetch();
    message.reply(fact);
  }

}
