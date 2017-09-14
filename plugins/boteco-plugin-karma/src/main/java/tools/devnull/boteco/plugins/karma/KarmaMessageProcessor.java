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

import tools.devnull.boteco.Name;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Group;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Group
@Name("karma")
public class KarmaMessageProcessor implements MessageProcessor {

  private final Pattern pattern =
      Pattern.compile("(?<term>\\S+)(?<operation>\\+\\+|--)(\\s|[,.:;!?]|$)");

  private final KarmaRepository repository;

  public KarmaMessageProcessor(KarmaRepository repository) {
    this.repository = repository;
  }

  @Override
  public void process(IncomeMessage message) {
    Matcher matcher = pattern.matcher(message.content());
    StringBuilder replyMessage = new StringBuilder();
    Set<String> evaluated = new HashSet<>();
    while (matcher.find()) {
      String term = matcher.group("term");
      String operation = matcher.group("operation");
      if (!evaluated.contains(term.toLowerCase())) {
        int value = update(term, operation);
        replyMessage.append(buildReplyMessage(term, value)).append("\n");
        evaluated.add(term.toLowerCase());
      }
    }
    if (replyMessage.length() > 0) {
      message.sendBack(replyMessage.toString());
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

  private String buildReplyMessage(String term, int value) {
    String tag = value < 0 ? "n" : "p";
    return String.format("[a]%s[/a] has now [%s]%d[/%s] points of karma", term, tag, value, tag);
  }

  private int operateKarma(String term, Consumer<Karma> operation) {
    term = term.toLowerCase();
    Karma karma = repository.find(term);
    operation.accept(karma);
    repository.update(karma);
    return karma.value();
  }

}
