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

package tools.devnull.boteco.message;

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.Destination;
import tools.devnull.boteco.MessageLocation;
import tools.devnull.boteco.user.User;

import java.io.Serializable;

/**
 * Interface that defines a message that arrives from a channel.
 */
public interface IncomeMessage extends Serializable {

  /**
   * Returns the channel this message belongs.
   *
   * @return the channel this message belongs.
   */
  Channel channel();

  /**
   * Returns the content of this message.
   *
   * @return the content of this message.
   */
  String content();

  /**
   * Returns the sender of this message.
   *
   * @return the sender of this message.
   */
  Sender sender();

  /**
   * Returns the recognized user associated with this message
   *
   * @return the user that sent this message
   */
  User user();

  /**
   * Returns the target of this message.
   *
   * @return the target of this message.
   */
  String target();

  /**
   * Returns <code>true</code> if this message was sent in private.
   *
   * @return <code>true</code> if this message was sent in private
   */
  boolean isPrivate();

  /**
   * Returns <code>true</code> if this message was sent to a group.
   *
   * @return <code>true</code> if this message was sent to a group
   */
  boolean isGroup();

  /**
   * Checks if this message has a command in its content.
   *
   * @return <code>true</code> if this message.
   */
  boolean hasCommand();

  /**
   * Returns the command represented by this message.
   *
   * @return the command if this message is a {@link #hasCommand() command}
   */
  MessageCommand command();

  /**
   * Replies this message by sending the given content through the same
   * {@link #channel() channel} that this message was received.
   * <p>
   * The message will also have a mention to the sender.
   *
   * @param object the object that represents the message
   */
  void reply(Sendable object);

  /**
   * Sends back a message with the given content through the same
   * {@link #channel() channel} that this message was received.
   *
   * @param object the object that represents the message
   */
  void sendBack(Sendable object);

  /**
   * Wraps the given content as a {@link Sendable} and sends it as a reply to this message
   *
   * @param content the content to reply
   */
  default void reply(String content) {
    reply(Sendable.message(content));
  }

  /**
   * Wraps the given content as a {@link Sendable} and sends it back to the same channel.
   *
   * @param content the content to reply
   */
  default void sendBack(String content) {
    sendBack(Sendable.message(content));
  }

  /**
   * Formats the message using the given format and arguments and then
   * uses the produced content as the {@link #reply(String) reply}.
   *
   * @param format the format of the message
   * @param args   the arguments to format the message
   */
  default void reply(String format, Object... args) {
    reply(String.format(format, args));
  }

  /**
   * Formats the message using the given format and arguments and then
   * uses the produced content as the {@link #sendBack(String) reply to sender}.
   *
   * @param format the format of the message
   * @param args   the arguments to format the message
   */
  default void sendBack(String format, Object... args) {
    sendBack(String.format(format, args));
  }

  /**
   * Returns a message location based on this message. If you send a message to this
   * location, the sender will receive it.
   *
   * @return the message location for sending messages to the sender
   */
  default MessageLocation location() {
    return Destination.channel(channel().id()).to(sender().id());
  }

}
