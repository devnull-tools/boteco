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

package tools.devnull.boteco.storage;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

/**
 * Interface to select a value for an operation.
 *
 * @param <T> the type of the value
 */
public interface ValueSelector<T extends Storable> {

  /**
   * Retrieves the object that has the given id.
   *
   * @param id the id of the object to retrieve
   * @return the object that has the given id.
   */
  T id(Serializable id);

  /**
   * Retrieves all objects from the store.
   *
   * @return all objects from the store.
   */
  List<T> all();

  /**
   * Retrieves all objects that matches the given filter.
   *
   * @param filter the filter to match
   * @return all objects from the store that matches the given filter.
   */
  List<T> where(Predicate<? super T> filter);

}
