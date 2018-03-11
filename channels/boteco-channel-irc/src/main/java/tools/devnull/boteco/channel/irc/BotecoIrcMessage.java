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

package tools.devnull.boteco.channel.irc;

import org.apache.camel.component.irc.IrcMessage;
import org.schwering.irc.lib.IRCUser;
import tools.devnull.boteco.Channel;
import tools.devnull.boteco.message.Message;
import tools.devnull.boteco.message.Sender;

/**
 * An abstraction of an IRC message.
 */
public class BotecoIrcMessage implements Message {

  private static final long serialVersionUID = -4838114200533219628L;

  private final Channel channel;
  private final String message;
  private final Sender sender;
  private final String target;

  /**
   * Creates a new message using the given parameters
   *
   * @param channel the channel implementation
   * @param income  the actual IRC message
   */
  public BotecoIrcMessage(Channel channel,
                          IrcMessage income) {
    this.message = income.getMessage();
    this.sender = new IrcSender(income.getUser());
    this.target = income.getTarget();
    this.channel = channel;
  }

  @Override
  public Channel channel() {
    return channel;
  }

  @Override
  public String content() {
    return message;
  }

  @Override
  public Sender sender() {
    return this.sender;
  }

  @Override
  public String target() {
    return isPrivate() ? this.sender.id() : target;
  }

  @Override
  public boolean isPrivate() {
    return !isGroup();
  }

  @Override
  public boolean isGroup() {
    return target.startsWith("#");
  }

  @Override
  public String replyId() {
    return sender.id();
  }

  private static class IrcSender implements Sender {

    private static final long serialVersionUID = 6728222816835888595L;

    private final String name;
    private final String username;
    private final String nickname;

    private IrcSender(IRCUser user) {
      this.name = user.getUsername(); // IRCUser don't have this so use the username
      this.username = user.getUsername();
      this.nickname = user.getNick();
    }

    @Override
    public String id() {
      return nickname;
    }

    @Override
    public String name() {
      return name;
    }

    @Override
    public String username() {
      return username;
    }

    @Override
    public String toString() {
      return nickname;
    }

  }

}
