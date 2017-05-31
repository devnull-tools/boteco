package tools.devnull.boteco.plugins.definition.spi;

import tools.devnull.boteco.message.Sendable;

/**
 * Interface that describes a definition.
 */
public interface Definition extends Sendable {

  /**
   * Returns the term defined by this definition.
   *
   * @return the term defined by this definition.
   */
  String term();

  /**
   * Returns the source that defined this term
   *
   * @return the source that defined this term
   */
  String source();

  /**
   * Returns the definition itself.
   *
   * @return the definition itself.
   */
  String description();

  default String title() {
    return String.format("Definition for %s based on %s", term(), source());
  }

  default String message() {
    return description();
  }

}
