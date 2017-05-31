package tools.devnull.boteco.plugins.definition.spi;

/**
 * Interface that describes a definition.
 */
public interface Definition {

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

}
