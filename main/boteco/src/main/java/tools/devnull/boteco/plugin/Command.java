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

package tools.devnull.boteco.plugin;

import tools.devnull.boteco.BotException;

import java.util.Arrays;
import java.util.List;

/**
 * A class that represents command information for use by help interfaces.
 */
public class Command {

  private final String name;
  private final String description;
  private final String[] parameters;

  /**
   * Creates a new Command object
   *
   * @param name        the name of the command
   * @param description the description of the command
   * @param parameters  the parameters of the command
   */
  public Command(String name, String description, String... parameters) {
    this.name = name;
    this.description = description;
    this.parameters = parameters;
  }

  /**
   * Returns the name of this command
   *
   * @return the name of this command
   */
  public String name() {
    return name;
  }

  /**
   * Returns the description of this command
   *
   * @return the description of this command
   */
  public String description() {
    return description;
  }

  /**
   * Returns a list of the parameters for this command.
   *
   * @return a list of the parameters for this command
   */
  public List<String> parameters() {
    return Arrays.asList(parameters);
  }

  /**
   * Creates a Command object using a component
   *
   * @param name the command name
   * @return a component for defining the description
   */
  public static CommandBuilder command(String name) {
    return new CommandBuilder() {
      @Override
      public CommandBuilder with(String... parameters) {
        return new CommandBuilder() {
          @Override
          public CommandBuilder with(String... parameters) {
            throw new BotException("Parameters already defined");
          }

          @Override
          public Command does(String description) {
            return new Command(name, description, parameters);
          }
        };
      }

      @Override
      public Command does(String description) {
        return new Command(name, description);
      }
    };
  }

  /**
   * Interface for helping create Command objects
   */
  public interface CommandBuilder {

    /**
     * Defines the parameters of the command.
     *
     * @param parameters the parameters of the command
     * @return a builder instance
     */
    CommandBuilder with(String... parameters);

    /**
     * Defines what this command does when used
     *
     * @param description the description of the command
     * @return the created command object
     */
    Command does(String description);

  }

}
