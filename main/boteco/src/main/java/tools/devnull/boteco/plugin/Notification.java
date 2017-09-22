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
 * A class that represents notification information for use by help interfaces
 */
public class Notification {

  private final String name;
  private final String description;

  /**
   * Creates a new Notification object
   *
   * @param name        the name of the notification
   * @param description the description of the notification
   */
  public Notification(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Returns the name of this notification
   *
   * @return the name of this notification
   */
  public String name() {
    return name;
  }

  /**
   * Returns the description of this notification
   *
   * @return the description of this notification
   */
  public String description() {
    return description;
  }

  /**
   * Creates a Notification object using a component
   *
   * @param name the name of the notification
   * @return a component for defining the description
   */
  public static NotificationBuilder notifies(String name) {
    return description -> new Notification(name, description);
  }

  /**
   * Interface for helping create Command objects
   */
  public interface NotificationBuilder {

    /**
     * Defines what is advised by this notification
     *
     * @param description the description of the notification
     * @return the created notification
     */
    Notification about(String description);

  }

}
