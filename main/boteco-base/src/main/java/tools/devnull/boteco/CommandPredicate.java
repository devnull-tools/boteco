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

import java.util.function.Predicate;

/**
 * Interface that can create predicates for {@link Command} objects.
 */
public interface CommandPredicate extends Predicate<IncomeMessage> {

  /**
   * Returns a new predicate that accepts any arguments
   *
   * @return a new predicate that accepts any arguments
   */
  default Predicate<IncomeMessage> withArgs() {
    return this.and(message -> message.command().hasArgs());
  }

  /**
   * Returns a new predicate that accepts arguments that matches the given filter
   *
   * @param predicate the filter to test
   * @return a new predicate that accepts arguments that matches the given filter
   */
  default Predicate<IncomeMessage> withArgs(Predicate<String> predicate) {
    return this.and(message -> message.command().args().stream().allMatch(predicate));
  }

  /**
   * Returns a new predicate that rejects any arguments
   *
   * @return a new predicate that rejects any arguments
   */
  default Predicate<IncomeMessage> withoutArgs() {
    return this.and(message -> !message.command().hasArgs());
  }

}
