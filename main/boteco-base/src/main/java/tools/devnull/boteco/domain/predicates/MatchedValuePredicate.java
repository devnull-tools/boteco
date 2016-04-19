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

package tools.devnull.boteco.domain.predicates;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A predicate that accepts values if they matches a given patter.
 *
 * @param <E> the type of the target object
 */

public class MatchedValuePredicate<E> implements Predicate<E> {

  private final String expression;
  private final Function<E, String> function;

  /**
   * Creates a new predicate using the given pattern and the function to extract
   * the value from the targets to evaluate.
   *
   * @param expression the pattern to match
   * @param function   the function that should be used to extract the
   *                   value from the target to evaluate
   */
  public MatchedValuePredicate(String expression, Function<E, String> function) {
    this.expression = expression;
    this.function = function;
  }


  @Override
  public boolean test(E object) {
    return function.apply(object).matches(expression);
  }

}
