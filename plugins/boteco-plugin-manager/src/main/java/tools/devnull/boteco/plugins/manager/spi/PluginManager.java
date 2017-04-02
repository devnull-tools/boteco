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

package tools.devnull.boteco.plugins.manager.spi;

import tools.devnull.boteco.MessageLocation;

/**
 * Interface that defines a plugin manager that acts for specific channels.
 * <p>
 * Note that for global management, the best is to use the runtime platform itself.
 */
public interface PluginManager {

  /**
   * Disables the given plugin name to run for the given message location
   *
   * @param name     the name of the message processor
   * @param location the message location to refer to
   */
  void disable(String name, MessageLocation location);

  /**
   * Returns {@code true} if the given message processor's name is active
   * for the given location.
   *
   * @param name     the name of the message processor
   * @param location the message location to refer to
   * @return {@code true} if the referring parameters represents an active message processor
   */
  boolean isEnabled(String name, MessageLocation location);

  /**
   * Enables the given plugin name to run for the given message location
   *
   * @param name     the name of the message processor
   * @param location the message location to refer to
   */
  void enable(String name, MessageLocation location);

}
