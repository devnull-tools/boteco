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

import java.util.Collections;
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
   * Returns all aspects of messages that this plugin listens to
   * in order to provide its capabilities.
   *
   * @return what this plugin listens in messages
   */
  default List<Listener> listensTo() {
    return Collections.emptyList();
  }

  /**
   * Returns a list of the commands this plugin offers to interact.
   *
   * @return a list containing the commands provided by this plugin.
   */
  default List<Command> availableCommands() {
    return Collections.emptyList();
  }

  /**
   * Indicates the {@link tools.devnull.boteco.provider.Provider providers} used by
   * this plugin (if any).
   *
   * @return a list containing the provider types used by this plugin.
   */
  default List<String> providerTypes() {
    return Collections.emptyList();
  }

  /**
   * Returns a list of the notifications that this plugin sends. The user may
   * subscribe to those notifications in order to receive them.
   *
   * @return a list of notifications that this plugin sends.
   */
  default List<Notification> notifications() {
    return Collections.emptyList();
  }

}
