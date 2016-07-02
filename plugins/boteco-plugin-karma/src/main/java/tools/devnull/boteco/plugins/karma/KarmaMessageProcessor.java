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

package tools.devnull.boteco.plugins.karma;

import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;

import java.util.Properties;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KarmaMessageProcessor implements MessageProcessor {

  private final Pattern pattern =
      Pattern.compile("(?<term>\\S+)(?<operation>\\+\\+|\\-\\-)(\\s|[,.:;!?]|$)");

  private final KarmaRepository repository;
  private final Properties properties;

  public KarmaMessageProcessor(KarmaRepository repository, Properties properties) {
    this.repository = repository;
    this.properties = properties;
  }

  @Override
  public String id() {
    return "karma";
  }

  @Override
  public boolean canProcess(IncomeMessage message) {
    return message.isGroup() && pattern.matcher(message.content()).find();
  }

  @Override
  public void process(IncomeMessage message) {
    Matcher matcher = pattern.matcher(message.content());
    while (matcher.find()) {
      String term = matcher.group("term");
      String operation = matcher.group("operation");
      int value = update(term, operation);
      reply(message, term, value);
    }
  }

  private int update(String term, String operation) {
    int updatedValue = 0;
    switch (operation) {
      case "++":
        updatedValue = operateKarma(term, Karma::raise);
        break;
      case "--":
        updatedValue = operateKarma(term, Karma::lower);
        break;
    }
    return updatedValue;
  }

  private void reply(IncomeMessage message, String term, int value) {
    String key = term.toLowerCase();
    String content = properties.getProperty(key, "[a]%t[/a] has now %n %u of %k");
    String tag = value < 0 ? "n" : "p";
    content = content.replace("%t", properties.getProperty(key + ".term", term));
    content = content.replace("%n", String.format("[%s]%d[/%s]", tag, value, tag));
    content = content.replace("%u", properties.getProperty(key + ".unit", "points"));
    content = content.replace("%k", properties.getProperty(key + ".karma", "karma"));
    message.sendBack(content);
  }

  private int operateKarma(String term, Consumer<Karma> operation) {
    term = term.toLowerCase();
    Karma karma = repository.find(term);
    operation.accept(karma);
    repository.update(karma);
    return karma.value();
  }

}
