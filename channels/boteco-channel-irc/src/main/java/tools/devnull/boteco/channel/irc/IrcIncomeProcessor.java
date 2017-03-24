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

package tools.devnull.boteco.channel.irc;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import tools.devnull.boteco.Channel;
import tools.devnull.boteco.Destination;
import tools.devnull.boteco.user.User;
import tools.devnull.boteco.user.UserManager;
import tools.devnull.boteco.message.MessageDispatcher;

/**
 * A processor that dispatches income IRC messages for the bot to process.
 */
public class IrcIncomeProcessor implements Processor {

  private final Channel channel;
  private final MessageDispatcher dispatcher;
  private final UserManager userManager;

  /**
   * Creates a new processor based on the giving parameters
   *
   * @param channel     the channel implementation
   * @param dispatcher  the component to dispatch the messages to be processed
   * @param userManager the user manager to retrieve the registered users
   */
  public IrcIncomeProcessor(Channel channel,
                            MessageDispatcher dispatcher,
                            UserManager userManager) {
    this.channel = channel;
    this.dispatcher = dispatcher;
    this.userManager = userManager;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    org.apache.camel.component.irc.IrcMessage income = exchange.getIn(org.apache.camel.component.irc.IrcMessage.class);
    if (income.getMessage() != null && !income.getMessage().isEmpty()) {
      User user = this.userManager.find(Destination.channel(IrcChannel.ID).to(income.getUser().getNick()));
      dispatcher.dispatch(new BotecoIrcMessage(channel, income, user));
    }
  }

}
