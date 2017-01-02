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

import java.util.Dictionary;
import java.util.Properties;

public class OsgiServiceDefinition<T> implements ServiceDefinition<T> {

  private final BundleContext bundleContext;
  private final T implementation;
  private final Dictionary properties;

  public OsgiServiceDefinition(BundleContext bundleContext, T implementation) {
    this.bundleContext = bundleContext;
    this.implementation = implementation;
    this.properties = new Properties();
  }

  @Override
  public ServiceDefinition<T> withProperty(String key, Object value) {
    this.properties.put(key, value);
    return this;
  }

  @Override
  public ServiceDefinition<T> withId(Object value) {
    return withProperty("id", value);
  }

  @Override
  public void as(Class<T> serviceClass) {
    bundleContext.registerService(serviceClass, implementation, properties);
  }

}
