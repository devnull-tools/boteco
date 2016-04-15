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

package tools.devnull.boteco.domain;

import java.io.Serializable;

/**
 * Interface that defines a channel.
 * <p>
 * Channels can receive and/or send messages. A channel
 * that only receives messages could be a sensor and a
 * channel that only sends messages could be a notification
 * service. Chat platforms usually have a duplex channel
 * (can send and receive messages).
 */
public interface Channel extends Serializable {

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
   * Returns a component capable of format a message
   * or part of a message. Useful to send a rich content.
   *
   * @return a component to format messages
   */
  ContentFormatter formatter();

  /**
   * Returns <code>true</code> if the channel is capable of
   * sending messages, <code>false</code> otherwise.
   *
   * @return <code>true</code> if the channel is capable of
   * sending messages, <code>false</code> otherwise.
   */
  boolean isOut();

  /**
   * Sends a message to a given target through this channel if
   * the channel {@link #isOut() can send messages}.
   *
   * @param content the content of the message
   * @return a component to select the target
   */
  TargetSelector send(String content);

}
