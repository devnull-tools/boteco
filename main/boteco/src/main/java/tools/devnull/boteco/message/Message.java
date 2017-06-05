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

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.user.User;

import java.io.Serializable;

/**
 * Interface that defines a message sent to the bot.
 */
public interface Message extends Serializable {

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
   * Return the id of this message for replying purposes. This may
   * be null depending on the channel.
   *
   * @return the id for replying to this message.
   * @see OutcomeMessageConfiguration#replyingTo(String)
   */
  String replyId();

}
