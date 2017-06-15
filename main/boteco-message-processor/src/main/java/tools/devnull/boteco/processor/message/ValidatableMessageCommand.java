/*
 * The MIT License
 *
 * Copyright (c) 2016 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
 *
 * Permission  is hereby granted, free of charge, to any person obtaining
 * a  copy  of  this  software  and  associated  documentation files (the
 * "Software"),  to  deal  in the Software without restriction, including
 * without  limitation  the  rights to use, copy, modify, merge, publish,
 * distribute,  sublicense,  and/or  sell  copies of the Software, and to
 * permit  persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * The  above  copyright  notice  and  this  permission  notice  shall be
 * included  in  all  copies  or  substantial  portions  of the Software.
 *
 * THE  SOFTWARE  IS  PROVIDED  "AS  IS",  WITHOUT  WARRANTY OF ANY KIND,
 * EXPRESS  OR  IMPLIED,  INCLUDING  BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN  NO  EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM,  DAMAGES  OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT  OR  OTHERWISE,  ARISING  FROM,  OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE   OR   THE   USE   OR   OTHER   DEALINGS  IN  THE  SOFTWARE.
 */

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
