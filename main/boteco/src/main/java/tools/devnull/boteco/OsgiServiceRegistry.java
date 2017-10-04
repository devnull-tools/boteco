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

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import tools.devnull.boteco.provider.ProvidedBy;
import tools.devnull.boteco.provider.Provider;
import tools.devnull.trugger.Optional;

import java.util.List;

import static tools.devnull.boteco.Predicates.eq;
import static tools.devnull.boteco.Predicates.id;
import static tools.devnull.boteco.Predicates.serviceProperty;
import static tools.devnull.boteco.Predicates.type;

/**
 * An implementation of a service registry that uses the OSGi Registry
 */
public class OsgiServiceRegistry implements ServiceRegistry {

  private static final long serialVersionUID = -7905765563295691457L;

  public <T> ServiceQuery<T> locate(Class<T> serviceClass) {
    BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
    return new OsgiServiceQuery<>(bundleContext, serviceClass);
  }

  @Override
  public <T> ServiceDefinition<T> register(T implementation) {
    BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
    return new OsgiServiceDefinition<>(bundleContext, implementation);
  }

  @Override
  public <T> void registerProvider(Class<? super T> typeClass, Provider<T> provider) {
    register(provider)
        .withProperty("type", resolveTypeAttribute(typeClass))
        .withId(provider.id())
        .as(Provider.class);
  }

  @Override
  public <T> Optional<Provider<T>> providerOf(Class<T> objectClass) {
    String providerType = resolveTypeAttribute(objectClass);
    Optional provider = locate(Provider.class)
        .filter(type(providerType).and(serviceProperty("default", eq("true"))))
        .one();
    return provider;
  }

  @Override
  public <T> Optional<Provider<T>> providerOf(Class<T> objectClass, String providerId) {
    String providerType = resolveTypeAttribute(objectClass);
    Optional provider = locate(Provider.class)
        .filter(type(providerType).and(id(providerId)))
        .one();
    return provider;
  }

  @Override
  public <T> List<Provider<T>> providersOf(Class<T> objectClass) {
    String providerType = resolveTypeAttribute(objectClass);
    List result = locate(Provider.class)
        .filter(type(providerType))
        .all();
    return result;
  }

  private <T> String resolveTypeAttribute(Class<T> objectClass) {
    String type = objectClass.getSimpleName();
    if (objectClass.isAnnotationPresent(ProvidedBy.class)) {
      type = objectClass.getAnnotation(ProvidedBy.class).value();
    }
    return type;
  }

}
