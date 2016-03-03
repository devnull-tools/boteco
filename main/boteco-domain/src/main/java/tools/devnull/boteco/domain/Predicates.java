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

package tools.devnull.boteco.domain;

import tools.devnull.boteco.domain.predicates.ChannelPredicate;
import tools.devnull.boteco.domain.predicates.CommandPredicate;
import tools.devnull.boteco.domain.predicates.ContentPredicate;
import tools.devnull.boteco.domain.predicates.SenderPredicate;
import tools.devnull.boteco.domain.predicates.TargetPredicate;

import java.util.function.Predicate;

/**
 * An utility class to hold predicates.
 */
public class Predicates {

  public static Predicate<IncomeMessage> channel(String acceptedValue) {
    return new ChannelPredicate(acceptedValue);
  }

  public static Predicate<IncomeMessage> channels(String... acceptedValues) {
    return new ChannelPredicate(acceptedValues);
  }

  public static Predicate<IncomeMessage> command(String acceptedValue) {
    return new CommandPredicate(acceptedValue);
  }

  public static Predicate<IncomeMessage> commands(String... acceptedValues) {
    return new CommandPredicate(acceptedValues);
  }

  public static Predicate<IncomeMessage> target(String acceptedValue) {
    return new TargetPredicate(acceptedValue);
  }

  public static Predicate<IncomeMessage> targets(String... acceptedValues) {
    return new TargetPredicate(acceptedValues);
  }

  public static Predicate<IncomeMessage> sender(String acceptedValue) {
    return new SenderPredicate(acceptedValue);
  }

  public static Predicate<IncomeMessage> senders(String... acceptedValues) {
    return new SenderPredicate(acceptedValues);
  }

  public static Predicate<IncomeMessage> content(String expression) {
    return new ContentPredicate(expression);
  }

}
