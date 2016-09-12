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

package tools.devnull.boteco.channel.telegram;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import tools.devnull.boteco.Destination;
import tools.devnull.boteco.user.User;
import tools.devnull.boteco.user.UserManager;
import tools.devnull.boteco.message.CommandExtractor;
import tools.devnull.boteco.ServiceLocator;
import tools.devnull.boteco.message.MessageDispatcher;

/**
 * A processor that deals with income messages from Telegram.
 */
public class TelegramIncomeProcessor implements Processor {

  private final CommandExtractor extractor;
  private final TelegramOffsetManager offsetManager;
  private final MessageDispatcher dispatcher;
  private final ServiceLocator serviceLocator;
  private final UserManager userManager;

  /**
   * Creates a new processor based on the given parameters
   *
   * @param extractor      a component for extracting commands from messages
   * @param offsetManager  a component to manager the current offset in poll operations
   * @param dispatcher     a component to dispatch messages to be processed
   * @param serviceLocator a service locator for lookup purposes
   * @param userManager    a user manager to fetch user information
   */
  public TelegramIncomeProcessor(CommandExtractor extractor,
                                 TelegramOffsetManager offsetManager,
                                 MessageDispatcher dispatcher,
                                 ServiceLocator serviceLocator,
                                 UserManager userManager) {
    this.extractor = extractor;
    this.offsetManager = offsetManager;
    this.dispatcher = dispatcher;
    this.serviceLocator = serviceLocator;
    this.userManager = userManager;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    offsetManager.process(exchange.getIn().getBody(TelegramPolling.class),
        pooling -> {
          String senderId = pooling.getMessage().getChat().getId().toString();
          User user = this.userManager.find(Destination.channel(TelegramChannel.ID).to(senderId));
          dispatcher.dispatch(new TelegramIncomeMessage(extractor, pooling.getMessage(), serviceLocator, user));
        });
  }

}
