package tools.devnul.boteco.plugins.validator;

import tools.devnull.boteco.OsgiFilter;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.trugger.util.factory.Context;
import tools.devnull.trugger.util.factory.ContextFactory;
import tools.devnull.trugger.util.factory.DefaultContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import java.lang.reflect.Parameter;

/**
 * A constraint validator factory that resolves OSGi services
 */
public class OsgiConstraintValidatorFactory implements ConstraintValidatorFactory {

  private final ServiceRegistry registry;
  private final ContextFactory contextFactory;

  public OsgiConstraintValidatorFactory(ServiceRegistry registry) {
    this.registry = registry;
    this.contextFactory = new ContextFactory(createContext());
  }

  @Override
  public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
    return contextFactory.create(key);
  }

  @Override
  public void releaseInstance(ConstraintValidator<?, ?> instance) {

  }

  private Context createContext() {
    return new DefaultContext()
        .use(this::resolve)
        .byDefault();
  }

  private Object resolve(Parameter parameter) {
    String query = null;
    if (parameter.isAnnotationPresent(OsgiFilter.class)) {
      query = parameter.getAnnotation(OsgiFilter.class).value();
    }
    return this.registry.locate(parameter.getType()).filter(query).one();
  }

}
