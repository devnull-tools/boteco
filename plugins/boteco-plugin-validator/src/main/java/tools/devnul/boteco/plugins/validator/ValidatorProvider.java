package tools.devnul.boteco.plugins.validator;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static java.util.Collections.singletonList;

/**
 * Class that provides a javax.validator.Validator object.
 */
public class ValidatorProvider {

  public static Validator create() {
    Configuration<?> configuration = Validation.byDefaultProvider().providerResolver(
        () -> singletonList(new HibernateValidator())
    ).configure();
    ValidatorFactory factory = configuration
        .messageInterpolator(new ParameterMessageInterpolator())
        .buildValidatorFactory();
    return factory.getValidator();
  }

}
