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

import org.osgi.framework.ServiceReference;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Interface for querying services in a {@link ServiceRegistry}
 */
public interface ServiceQuery<T> {

  /**
   * Adds the given filter to test services before returning.
   * <p>
   * Returned services are guarantee to match the given predicate.
   *
   * @param predicate the predicate to use
   * @return a new query that uses the given predicate
   */
  ServiceQuery<T> filter(Predicate<ServiceReference> predicate);

  /**
   * Return just one service found by this query, no matter
   * if there is more than one.
   *
   * @return the first service found
   */
  T one();

  /**
   * If no object is found, then return the given value.
   * <p>
   * This only applies when selecting {@link #one()} service.
   *
   * @param returnValue the value to return
   * @return the found service or the default return
   */
  T orElseReturn(T returnValue);

  /**
   * If no object is found, then return the value obtained by
   * the given supplier.
   * <p>
   * This only applies when selecting {@link #one()} service.
   *
   * @param supplier the supplier to provide the value
   * @return the found service or the default return
   */
  T orElse(Supplier<T> supplier);

  /**
   * Return all services found by this query
   *
   * @return all services found by this query
   */
  List<T> all();

}
