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

import java.io.Serializable;
import java.util.List;

/**
 * Interface to locate services in bundles.
 */
public interface ServiceLocator extends Serializable {

  /**
   * Locates the service that implements the given interface
   *
   * @param serviceClass the interface that the service implements
   * @param <T>          the type of the service
   * @return the located service
   */
  <T> T locate(Class<T> serviceClass);

  /**
   * Locates the first service that implements the given interface and matches the given filter.
   *
   * @param serviceClass the interface that the service implements
   * @param filter       the filter to match
   * @param <T>          the type of the service
   * @return the first service that implements the given interface and matches the given filter.
   */
  <T> T locate(Class<T> serviceClass, String filter);

  /**
   * Locates the services that implements the given interface and matches the given filter.
   *
   * @param serviceClass the interface that the service implements
   * @param filter       the filter to match
   * @param <T>          the type of the service
   * @return the services that implements the given interface and matches the given filter.
   */
  <T> List<T> locateAll(Class<T> serviceClass, String filter);

}
