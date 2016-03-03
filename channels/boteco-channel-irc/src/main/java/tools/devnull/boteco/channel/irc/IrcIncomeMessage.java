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

import org.apache.camel.Exchange;
import org.apache.camel.component.irc.IrcMessage;
import tools.devnull.boteco.domain.Command;
import tools.devnull.boteco.domain.CommandExtractor;
import tools.devnull.boteco.domain.IncomeMessage;

/**
 * An abstraction of an IRC message.
 */
public class IrcIncomeMessage implements IncomeMessage {

  private final Exchange exchange;
  private final IrcMessage income;
  private final CommandExtractor commandExtractor;

  public IrcIncomeMessage(IrcMessage income, CommandExtractor commandExtractor) {
    this.commandExtractor = commandExtractor;
    this.exchange = income.getExchange();
    this.income = income;
  }

  @Override
  public String channel() {
    return "irc";
  }

  @Override
  public String content() {
    return income.getMessage();
  }

  @Override
  public String sender() {
    return income.getUser().getNick();
  }

  @Override
  public String target() {
    return income.getTarget();
  }

  public boolean isPrivate() {
    return !target().startsWith("#");
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
    IrcMessage outcome = new IrcMessage();
    outcome.setMessage(content);
    if (isPrivate()) {
      outcome.setTarget(sender());
    } else {
      outcome.setTarget(income.getTarget());
    }
    outcome.setMessageType("PRIVMSG");
    exchange.setOut(outcome);
  }

}
