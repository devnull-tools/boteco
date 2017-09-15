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

import java.util.List;

/**
 * Interface that defines a plugin of the boteco's platform. Every plugin should
 * define an implementation of this interface in order to guide users to use it.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public interface Plugin {

  /**
   * Returns the unique id of this plugin.
   *
   * @return the id of this plugin.
   */
  String id();

  /**
   * Describes this plugin in a short way.
   *
   * @return the plugin's description
   */
  String description();

  /**
   * Returns a list of the commands this plugin offers to interact.
   *
   * @return a list containing the commands provided by this plugin.
   */
  List<Command> availableCommands();

  /**
   * Indicates if this plugin listens for messages in order to interact.
   * <p>
   * Some plugins can work without a command to advertise or complement
   * some information.
   *
   * @return {@code true} if this plugin can respond to messages without commands
   */
  boolean listenToMessages();

  /**
   * Indicates if this plugin sends notifications that users can subscribe to.
   *
   * @return {@code true} if this plugin sends notifications
   */
  boolean sendsNotifications();

  /**
   * Returns a list of the notifications that this plugin sends. The user may
   * subscribe to those notifications in order to receive them.
   *
   * @return a list of notifications that this plugin sends.
   */
  List<Notification> notifications();

}
