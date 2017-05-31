package tools.devnull.boteco.plugins.definition;

import tools.devnull.boteco.Name;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;
import tools.devnull.boteco.plugins.definition.spi.Definition;

import java.util.List;

@Name("lookup")
@Command("lookup")
public class LookupDefinitionMessageProcessor implements MessageProcessor {

  @Override
  public void process(IncomeMessage message) {
    Lookup command = message.command().as(Lookup.class);
    List<Definition> definitions = command.lookup();
    if (definitions.isEmpty()) {
      message.reply("Cannot find definitions for term " + command.term());
    } else {
      definitions.forEach(message::reply);
    }
  }

}
