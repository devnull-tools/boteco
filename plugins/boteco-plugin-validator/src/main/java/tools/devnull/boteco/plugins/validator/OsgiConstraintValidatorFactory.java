package tools.devnull.boteco.plugins.validator;

import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.util.OsgiParameterResolver;
import tools.devnull.trugger.util.factory.Context;
import tools.devnull.trugger.util.factory.ContextFactory;
import tools.devnull.trugger.util.factory.DefaultContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

/**
 * A constraint validator factory that resolves OSGi services
 */
public class OsgiConstraintValidatorFactory implements ConstraintValidatorFactory {

  private final ContextFactory contextFactory;
  private final OsgiParameterResolver osgiResolver;

  public OsgiConstraintValidatorFactory(ServiceRegistry registry) {
    this.contextFactory = new ContextFactory(createContext());
    this.osgiResolver = new OsgiParameterResolver(registry);
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
        .use(osgiResolver)
        .byDefault();
  }

}
