package tools.devnull.boteco.util;

import tools.devnull.boteco.OsgiFilter;
import tools.devnull.boteco.ServiceRegistry;

import java.lang.reflect.Parameter;
import java.util.function.Function;

/**
 * A parameter resolver that uses an OSGi Registry
 *
 * @see OsgiFilter
 */
public class OsgiParameterResolver implements Function<Parameter, Object> {

  private final ServiceRegistry serviceRegistry;

  public OsgiParameterResolver(ServiceRegistry serviceRegistry) {
    this.serviceRegistry = serviceRegistry;
  }

  @Override
  public Object apply(Parameter parameter) {
    String query = null;
    if (parameter.isAnnotationPresent(OsgiFilter.class)) {
      query = parameter.getAnnotation(OsgiFilter.class).value();
    }
    return this.serviceRegistry.locate(parameter.getType()).filter(query).one();
  }

}
