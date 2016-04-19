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
import tools.devnull.boteco.Channel;
import tools.devnull.boteco.Command;
import tools.devnull.boteco.CommandExtractor;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.ServiceLocator;

/**
 * An abstraction of an IRC message.
 */
public class IrcIncomeMessage implements IncomeMessage, ServiceLocator {

  private static final long serialVersionUID = -4838114200533219628L;

  private final Channel channel = new IrcChannel();
  private final CommandExtractor commandExtractor;
  private final String message;
  private final String user;
  private final String target;

  public IrcIncomeMessage(IrcMessage income, CommandExtractor commandExtractor) {
    this.commandExtractor = commandExtractor;
    this.message = income.getMessage();
    this.user = income.getUser().getNick();
    this.target = income.getTarget();
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
  public String sender() {
    return user;
  }

  @Override
  public String target() {
    return target;
  }

  @Override
  public boolean isPrivate() {
    return !isGroup();
  }

  @Override
  public boolean isGroup() {
    return target().startsWith("#");
  }

  @Override
  public boolean hasCommand() {
    return commandExtractor.isCommand(content());
  }

  @Override
  public Command command() {
    return commandExtractor.extract(content());
  }

  @Override
  public void reply(String content) {
    if (isPrivate()) {
      replySender(content);
    } else {
      send(target(), content);
    }
  }

  @Override
  public void replySender(String content) {
    send(sender(), content);
  }

  private void send(String target, String content) {
    channel().send(content).to(target);
  }

}
