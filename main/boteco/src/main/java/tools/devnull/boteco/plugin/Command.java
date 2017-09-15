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

/**
 * A class that represents command information for use by help interfaces.
 */
public class Command {

  private final String name;
  private final String description;

  /**
   * Creates a new Command object
   *
   * @param name        the name of the command
   * @param description the description of the command
   */
  public Command(String name, String description) {
    this.name = name;
    this.description = description;
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
   * Creates a Command object using a component
   *
   * @param name the command name
   * @return a component for defining the description
   */
  public static CommandBuilder command(String name) {
    return description -> new Command(name, description);
  }

  /**
   * Interface for helping create Command objects
   */
  public interface CommandBuilder {

    /**
     * Defines what this command does when used
     *
     * @param description the description of the command
     * @return the created command object
     */
    Command does(String description);

  }

}
