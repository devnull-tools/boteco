/*
 * The MIT License
 *
 * Copyright (c) 2016-2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.boteco.client.rest;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Interface that defines a result to a REST invocation
 *
 * @param <E> The type of the result
 */
public interface RestResult<E> {

  /**
   * Returns the result of the rest invocation.
   *
   * @return the result of the rest invocation.
   */
  E result();

  /**
   * Invokes the given consumer passing the result.
   *
   * @param consumer the consumer to use
   * @return an instance of this object.
   */
  RestResult and(Consumer<E> consumer);

  /**
   * Executes the given action in case of no result from the REST invocation
   *
   * @param action the action to execute.
   */
  void orElse(Runnable action);

  /**
   * Uses the given supplier to return a value in case of no result from the REST invocation
   *
   * @param supplier the supplier to use for retrieving the result
   * @return the value returned by the supplier.
   */
  E orElse(Supplier<E> supplier);

}
