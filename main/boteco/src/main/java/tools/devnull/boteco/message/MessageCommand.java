/*
 * The MIT License
 *
 * Copyright (c) 2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.boteco.message;

import java.util.List;
import java.util.function.Consumer;

/**
 * Interface that defines a command sent in a message.
 */
public interface MessageCommand {

  /**
   * Returns the command name
   *
   * @return the command name
   */
  String name();

  /**
   * Converts the input parameters to an object of the given class.
   *
   * @param target the type of the object that should be created.
   * @return an object that represents the input parameters of this command.
   */
  <E> E as(Class<E> target);

  /**
   * Returns the input as a list of arguments.
   *
   * @return a list of the arguments
   */
  List<String> asList();

  /**
   * Maps an action to execute when {@link #execute()} method is called. The parameters
   * will be converted to the given type and passed to the given action.
   *
   * @param actionName    the action name to map
   * @param parameterType the parameter type of the action parameters
   * @param action        the action to execute
   * @return a reference to this object
   * @see #execute()
   */
  <T> MessageCommand on(String actionName, Class<T> parameterType, Consumer<T> action);

  /**
   * Maps an action to execute when {@link #execute()} method is called. This method
   * assumes that the parameter of the given action is a {@code String}.
   *
   * @param actionName the action name to map
   * @param action     the action to execute
   * @return a reference to this object
   * @see #execute()
   */
  MessageCommand on(String actionName, Consumer<String> action);

  /**
   * Maps an action to execute when {@link #execute()} method is called. This method
   * assumes that no parameters are used by this action.
   *
   * @param actionName the action name to map
   * @param action     the action to execute
   * @return a reference to this object
   * @see #execute()
   */
  MessageCommand on(String actionName, Action action);

  /**
   * Maps an action to execute when {@link #execute()} method is called. The action
   * will be instantiated using the income message content.
   *
   * @param actionName  the action name to map
   * @param actionClass the action class to execute
   * @return
   * @see #on(String, Class, Consumer)
   */
  MessageCommand on(String actionName, Class<? extends Action> actionClass);

  /**
   * Do something if none of the actions matches the message
   *
   * @param action the action to execute
   */
  void orElse(Consumer<String> action);

  /**
   * Replies to the message with the given content if none of the actions matches the message.
   *
   * @param message the message to reply to the sender
   */
  void orElseReturn(String message);

  /**
   * Treats the first parameter of this command as an action and the remaining as parameters of
   * this action.
   * <p>
   * Depending on the action name, a code will be executed based on mappings from the {@code on} methods.
   *
   * @see #on(String, Class, Consumer)
   * @see #on(String, Consumer)
   * @see #on(String, Action)
   */
  void execute();

}
