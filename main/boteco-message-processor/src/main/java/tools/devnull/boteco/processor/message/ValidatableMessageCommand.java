package tools.devnull.boteco.processor.message;

import tools.devnull.boteco.Action;
import tools.devnull.boteco.message.MessageCommand;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A composite Message Command
 */
public class ValidatableMessageCommand implements MessageCommand {

  private final Validator validator;
  private final MessageCommand command;

  public ValidatableMessageCommand(Validator validator, MessageCommand command) {
    this.validator = validator;
    this.command = command;
  }

  @Override
  public String name() {
    return command.name();
  }

  @Override
  public <E> E as(Class<E> target) {
    E object = command.as(target);
    Set<ConstraintViolation<E>> violations = validator.validate(object);
    if (violations.isEmpty()) {
      return object;
    }
    String errorMessage = "Invalid parameter(s):\n" + violations.stream()
        .map(violation -> String.format("- %s: %s", violation.getPropertyPath(), violation.getMessage()))
        .collect(Collectors.joining("\n"));
    throw new IllegalArgumentException(errorMessage);
  }

  @Override
  public List<String> asList() {
    return command.asList();
  }

  @Override
  public <T> MessageCommand on(String actionName, Class<T> parameterType, Consumer<T> action) {
    return command.on(actionName, parameterType, action);
  }

  @Override
  public MessageCommand on(String actionName, Consumer<String> action) {
    return command.on(actionName, action);
  }

  @Override
  public MessageCommand on(String actionName, Action action) {
    return command.on(actionName, action);
  }

  @Override
  public MessageCommand on(String actionName, Class<? extends Action> actionClass) {
    return command.on(actionName, actionClass);
  }

  @Override
  public void orElse(Consumer<String> action) {
    command.orElse(action);
  }

  @Override
  public void orElseReturn(String message) {
    command.orElseReturn(message);
  }

  @Override
  public void execute() {
    command.execute();
  }

}
