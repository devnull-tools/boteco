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

import tools.devnull.boteco.provider.Provider;
import tools.devnull.trugger.Optional;

import java.io.Serializable;
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
   * @return a component to configure the query
   */
  <T> ServiceQuery<T> locate(Class<T> serviceClass);

  /**
   * Registers the given service.
   *
   * @param implementation the service implementation
   * @param <T>            the service type
   * @return a component to define the service
   */
  <T> ServiceDefinition<T> register(T implementation);

  /**
   * Registers the given provider.
   *
   * @param typeClass       the type of the object being provided
   * @param provider        the provider
   * @param defaultProvider tells if this provider is the default one
   */
  <T> void registerProvider(Class<? super T> typeClass, Provider<T> provider, boolean defaultProvider);

  /**
   * Returns the default {@link Provider} of the given class.
   * <p>
   * The default provider has the "default" service property
   * equals to "true".
   *
   * @param objectClass the class of the object provided
   * @param <T>         the object's type
   * @return the provider
   */
  <T> Optional<Provider<T>> providerOf(Class<T> objectClass);

  /**
   * Returns the {@link Provider} of the given class with the given id
   *
   * @param objectClass the class of the object provided
   * @param id          the id of the provider
   * @param <T>         the object's type
   * @return the provider
   */
  <T> Optional<Provider<T>> providerOf(Class<T> objectClass, String id);

  /**
   * Returns all {@link Provider providers} of the given class
   *
   * @param <T> the object's type
   * @return the provider
   */
  <T> List<Provider<T>> providersOf(Class<T> objectClass);

}
