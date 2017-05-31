package tools.devnull.boteco.plugins.validator;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import tools.devnull.boteco.OsgiServiceRegistry;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static java.util.Collections.singletonList;

/**
 * Class that provides a javax.validator.Validator object. The validators can be created by using
 * OSGi Services dependencies.
 */
public class ValidatorProvider {

  public static Validator create() {
    Configuration<?> configuration = Validation.byDefaultProvider().providerResolver(
        () -> singletonList(new HibernateValidator())
    ).configure();
    ValidatorFactory factory = configuration
        .messageInterpolator(new ParameterMessageInterpolator())
        .constraintValidatorFactory(new OsgiConstraintValidatorFactory(new OsgiServiceRegistry()))
        .buildValidatorFactory();
    return factory.getValidator();
  }

}
