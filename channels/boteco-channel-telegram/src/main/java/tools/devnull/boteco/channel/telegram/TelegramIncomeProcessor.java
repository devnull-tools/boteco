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
import tools.devnull.boteco.CommandExtractor;
import tools.devnull.boteco.message.MessageDispatcher;

/**
 * A processor that deals with income messages from Telegram.
 */
public class TelegramIncomeProcessor implements Processor {

  private final CommandExtractor extractor;
  private final TelegramOffsetManager offsetManager;
  private final MessageDispatcher dispatcher;

  public TelegramIncomeProcessor(CommandExtractor extractor,
                                 TelegramOffsetManager offsetManager,
                                 MessageDispatcher dispatcher) {
    this.extractor = extractor;
    this.offsetManager = offsetManager;
    this.dispatcher = dispatcher;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    offsetManager.process(exchange.getIn().getBody(TelegramPooling.class),
        pooling -> dispatcher.dispatch(new TelegramIncomeMessage(extractor, pooling.getMessage())));
  }

}
