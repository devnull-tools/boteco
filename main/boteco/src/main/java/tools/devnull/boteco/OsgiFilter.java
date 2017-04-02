package tools.devnull.boteco;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that a filter should be applied to the annotated element.
 * <p>
 * If you're creating, for example, a Constraint Validator and needs
 * a OSGi service, you can annotate the constructor parameter with
 * this annotation and specify the OSGi filter to fetch the service.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface OsgiFilter {

  /**
   * The query to use
   *
   * @return the query to use
   */
  String value();

}
