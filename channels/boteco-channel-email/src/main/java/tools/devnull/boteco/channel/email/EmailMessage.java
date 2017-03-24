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

package tools.devnull.boteco.channel.email;

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.message.Message;
import tools.devnull.boteco.message.Sender;
import tools.devnull.boteco.user.User;

/**
 * An abstraction for an email message
 */
public class EmailMessage implements Message {

  private static final long serialVersionUID = 8180521354784182482L;

  private final User user;
  private final Channel channel;
  private final String content;
  private final Sender sender;
  private final String target;

  /**
   * Creates a new message using the given parameters
   *
   * @param content the content of the email
   * @param sender  the sender of the email
   * @param target  the target of the email
   * @param user    the user associated with this message (may be {@code null})
   */
  public EmailMessage(Channel channel,
                      String content,
                      EmailSender sender,
                      String target,
                      User user) {
    this.channel = channel;
    this.content = content;
    this.sender = sender;
    this.target = target;
    this.user = user;
  }

  @Override
  public User user() {
    return this.user;
  }

  @Override
  public Channel channel() {
    return this.channel;
  }

  @Override
  public String content() {
    return this.content;
  }

  @Override
  public Sender sender() {
    return this.sender;
  }

  @Override
  public String target() {
    return this.target;
  }

  @Override
  public boolean isPrivate() {
    return true;
  }

  @Override
  public boolean isGroup() {
    return false;
  }

  @Override
  public String replyTo() {
    return null;
  }

}
