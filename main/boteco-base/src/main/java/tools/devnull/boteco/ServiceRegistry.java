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
import java.util.Dictionary;
import java.util.List;

/**
 * Interface that defines a registry for finding or registering services
 */
public interface ServiceRegistry extends Serializable {

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
   * Locates the first service that implements the given interface and matches the given filter.
   *
   * @param serviceClass the interface that the service implements
   * @param filter       the filter to match
   * @param args         the arguments to format the filter
   * @param <T>          the type of the service
   * @return the first service that implements the given interface and matches the given filter.
   */
  default <T> T locate(Class<T> serviceClass, String filter, Object... args) {
    return this.locate(serviceClass, String.format(filter, args));
  }

  /**
   * Locates the services that implements the given interface and matches the given filter.
   *
   * @param serviceClass the interface that the service implements
   * @param filter       the filter to match
   * @param <T>          the type of the service
   * @return the services that implements the given interface and matches the given filter.
   */
  <T> List<T> locateAll(Class<T> serviceClass, String filter);

  /**
   * Locates the services that implements the given interface and matches the given filter.
   *
   * @param serviceClass the interface that the service implements
   * @param filter       the filter to match
   * @param args         the arguments to format the filter
   * @param <T>          the type of the service
   * @return the services that implements the given interface and matches the given filter.
   */
  default <T> List<T> locateAll(Class<T> serviceClass, String filter, Object... args) {
    return this.locateAll(serviceClass, String.format(filter, args));
  }

  /**
   * Locates the services that implements the given interface.
   *
   * @param serviceClass the interface that the service implements
   * @param <T>          the type of the service
   * @return the services that implements the given interface.
   */
  <T> List<T> locateAll(Class<T> serviceClass);

  /**
   * Registers the given service.
   *
   * @param serviceClass   the service class
   * @param implementation the service implementation
   * @param <E>            the service type
   */
  <E> void register(Class<E> serviceClass, E implementation);

  /**
   * Registers the given service.
   *
   * @param serviceClass   the service class
   * @param implementation the service implementation
   * @param properties     the service properties
   * @param <E>            the service type
   */
  <E> void register(Class<E> serviceClass, E implementation, Dictionary<String, ?> properties);

  /**
   * Registers the given service.
   *
   * @param serviceClass   the service class
   * @param implementation the service implementation
   * @param id             the service id
   * @param <E>            the service type
   */
  <E> void register(Class<E> serviceClass, E implementation, String id);

}
