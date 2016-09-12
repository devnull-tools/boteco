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

package tools.devnull.boteco.channel.user;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import tools.devnull.boteco.user.User;
import tools.devnull.boteco.user.UserManager;
import tools.devnull.boteco.message.MessageSender;
import tools.devnull.boteco.message.OutcomeMessage;

/**
 * A processor that uses the user's preferred channel for sending the messages
 */
public class UserOutcomeProcessor implements Processor {

  private final UserManager userManager;
  private final MessageSender messageSender;

  /**
   * Creates a new processor based on the given parameters
   *
   * @param userManager   the user manager to fetch user information
   * @param messageSender the message sender to use for sending messages
   */
  public UserOutcomeProcessor(UserManager userManager, MessageSender messageSender) {
    this.userManager = userManager;
    this.messageSender = messageSender;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    OutcomeMessage message = exchange.getIn().getBody(OutcomeMessage.class);
    if (message != null) {
      User user = this.userManager.find(message.getTarget());
      if (user != null) {
        this.messageSender.send(message.getContent()).to(user.primaryDestination());
      }
    }
  }

}
