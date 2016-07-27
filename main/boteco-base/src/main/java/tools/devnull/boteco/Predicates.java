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

package tools.devnull.boteco;

import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.predicates.AcceptedValuePredicate;
import tools.devnull.boteco.predicates.MatchedValuePredicate;

import java.util.function.Predicate;

/**
 * An utility class to hold predicates.
 */
public class Predicates {

  /**
   * Returns a predicate that allows channels that have the given id.
   *
   * @param channelId the channel id
   * @return a predicate that allows channels that have the given id.
   */
  public static Predicate<IncomeMessage> channel(String channelId) {
    return new AcceptedValuePredicate<>(channelId, m -> m.channel().id());
  }

  /**
   * Returns a predicate that allows messages that {@link IncomeMessage#hasCommand() have a command}.
   *
   * @return a predicate that allows messages that have a command.
   */
  public static Predicate<IncomeMessage> hasCommand() {
    return IncomeMessage::hasCommand;
  }

  /**
   * Returns a predicate that allows commands that have the given name.
   *
   * @param commandName the allowed name
   * @return a predicate that allows commands that have the given name.
   */
  public static Predicate<IncomeMessage> command(String commandName) {
    return message -> hasCommand()
        .and(new AcceptedValuePredicate<>(commandName.toLowerCase(), m -> m.command().name().toLowerCase()))
        .test(message);
  }

  /**
   * Returns a predicate that allows only {@link IncomeMessage#isGroup() group messages}
   *
   * @return a predicate that allows only group messages.
   */
  public static Predicate<IncomeMessage> groupMessage() {
    return IncomeMessage::isGroup;
  }

  /**
   * Returns a predicate that allows only {@link IncomeMessage#isPrivate() private messages}
   *
   * @return a predicate that allows only private messages.
   */
  public static Predicate<IncomeMessage> privateMessage() {
    return IncomeMessage::isPrivate;
  }

  /**
   * Returns a predicate that allows messages for a given target.
   *
   * @param targetName the target's name
   * @return a predicate that allows messages for a given target.
   */
  public static Predicate<IncomeMessage> target(String targetName) {
    return new AcceptedValuePredicate<>(targetName, IncomeMessage::target);
  }

  /**
   * Returns a predicate that allows messages that have a content like the given expression.
   *
   * @param expression the expression to test
   * @return a predicate that allows messages that have a content like the given expression.
   */
  public static Predicate<IncomeMessage> content(String expression) {
    return new MatchedValuePredicate<>(expression, IncomeMessage::content);
  }

}
