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

package tools.devnull.boteco;

import tools.devnull.boteco.message.CommandExtractor;

import java.io.Serializable;

/**
 * Interface that defines a channel.
 */
public interface Channel extends Serializable {

  /**
   * Tells if this channel can send messages.
   *
   * @return {@code true} if this channel can send messages.
   */
  boolean canSend();

  /**
   * Tells if this channel can receive messages.
   *
   * @return {@code true} if this channel can receive messages.
   */
  boolean canReceive();

  /**
   * Returns the name of the channel
   *
   * @return the name of the channel
   */
  String name();

  /**
   * Returns the id of the channel, used
   * to identify it through the platform.
   *
   * @return the id of the channel
   */
  String id();

  /**
   * Returns the command extractor for this channel.
   * <p>
   * This is only needed if the Channel can {@link #canReceive()} receive}
   * messages.
   *
   * @return the command extractor for this channel.
   */
  CommandExtractor commandExtractor();

}
