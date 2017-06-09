/*
 * The MIT License
 *
 * Copyright (c) 2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Interface that defines a result of an operation.
 *
 * @param <E> The type of the result
 */
public interface Result<E> {

  /**
   * Returns the result of the rest invocation.
   *
   * @return the result of the rest invocation.
   */
  E value();

  /**
   * Filters this result by testing the value against the
   * given predicate.
   * <p>
   * If the predicate matches the value, then the same instance
   * will be returned.
   *
   * @param predicate the predicate to test
   * @return an empty result if the predicate doesn't matches the value
   */
  default Result<E> filter(Predicate<? super E> predicate) {
    E value = value();
    if (value != null) {
      return predicate.test(value) ? this : Result.empty();
    }
    return this;
  }

  /**
   * Invokes the given consumer passing the result.
   * <p>
   * The consumer will be invoked only if this result
   * contains a non-null value.
   *
   * @param consumer the consumer to use
   * @return an instance of this object.
   */
  default Result<E> and(Consumer<E> consumer) {
    E value = value();
    if (value != null) {
      consumer.accept(value);
    }
    return this;
  }

  /**
   * Maps the value using the given function and return a new
   * result containing the new value.
   * <p>
   * The function will be invoked only if this result
   * contains a non-null value.
   *
   * @param function the function to map the value
   * @return a result containing the new value
   */
  default <T> Result<T> map(Function<? super E, ? extends T> function) {
    E value = value();
    if (value != null) {
      return Result.of(function.apply(value));
    }
    return Result.empty();
  }

  /**
   * Executes the given action in case of no result from the REST invocation
   *
   * @param action the action to execute.
   */
  default void orElse(Action action) {
    if (value() == null) {
      action.execute();
    }
  }

  /**
   * Uses the given supplier to return a value in case of no result from the REST invocation
   *
   * @param supplier the supplier to use for retrieving the result
   * @return the value returned by the supplier.
   */
  default E orElseReturn(Supplier<E> supplier) {
    E value = value();
    return value != null ? value : supplier.get();
  }

  /**
   * Throws the supplied exception if case of no result from the REST invocation
   *
   * @param exceptionSupplier the exception supplier
   * @return the result
   */
  default E orElseThrow(Supplier<? extends RuntimeException> exceptionSupplier) {
    E value = value();
    if (value == null) {
      throw exceptionSupplier.get();
    }
    return value;
  }

  /**
   * Creates a simple result containing the given value
   *
   * @param value the value of the result
   * @return a result object that holds the given value
   */
  static <E> Result<E> of(E value) {
    return () -> value;
  }

  /**
   * Wraps the given supplier as a Result object.
   *
   * @param supplier the supplier to get the value
   * @return the given supplier wrapped as a result.
   */
  static <E> Result<E> of(Supplier<E> supplier) {
    return supplier::get;
  }

  /**
   * Creates a result of a null value.
   *
   * @return a result of a null value.
   */
  static <E> Result<E> empty() {
    return () -> null;
  }

}
