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

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OsgiServiceQuery<T> implements ServiceQuery<T> {

  private final BundleContext bundleContext;
  private final Class<T> serviceClass;
  private final Predicate<ServiceReference> predicate;

  public OsgiServiceQuery(BundleContext bundleContext, Class<T> serviceClass) {
    this.bundleContext = bundleContext;
    this.serviceClass = serviceClass;
    this.predicate = null;
  }

  public OsgiServiceQuery(BundleContext bundleContext, Class<T> serviceClass,
                          Predicate<ServiceReference> predicate) {
    this.bundleContext = bundleContext;
    this.serviceClass = serviceClass;
    this.predicate = predicate;
  }

  @Override
  public ServiceQuery<T> filter(Predicate<ServiceReference> predicate) {
    return this.predicate == null ?
        new OsgiServiceQuery<>(this.bundleContext, this.serviceClass, predicate) :
        new OsgiServiceQuery<>(this.bundleContext, this.serviceClass, this.predicate.and(predicate));
  }

  @Override
  public T one() {
    if (this.predicate != null) {
      try {
        Collection<ServiceReference<T>> references = this.bundleContext.getServiceReferences(serviceClass, null);
        return references.stream()
            .filter(this.predicate)
            .findFirst()
            .map(this.bundleContext::getService)
            .orElseGet(null);
      } catch (InvalidSyntaxException e) {
        throw new BotException(e);
      }
    } else {
      ServiceReference<T> reference = this.bundleContext.getServiceReference(serviceClass);
      return this.bundleContext.getService(reference);
    }
  }

  @Override
  public List<T> all() {
    try {
      Collection<ServiceReference<T>> references = this.bundleContext.getServiceReferences(serviceClass, null);
      Stream<ServiceReference<T>> stream = references.stream();
      if (this.predicate != null) {
        stream = stream.filter(this.predicate);
      }
      return stream.map(this.bundleContext::getService)
          .collect(Collectors.toList());
    } catch (InvalidSyntaxException e) {
      throw new BotException(e);
    }
  }

  @Override
  public T orElseReturn(T returnValue) {
    return orElse(() -> returnValue);
  }

  @Override
  public T orElse(Supplier<T> supplier) {
    T service = one();
    return service != null ? service : supplier.get();
  }

}
