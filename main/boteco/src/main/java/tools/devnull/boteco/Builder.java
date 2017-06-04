package tools.devnull.boteco;

/**
 * Interface that defines a component capable of build another component
 * based on some provided values.
 */
public interface Builder<T> {

  T build();

}
