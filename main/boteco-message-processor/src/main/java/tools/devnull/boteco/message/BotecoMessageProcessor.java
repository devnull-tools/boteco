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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import tools.devnull.boteco.domain.IncomeMessage;
import tools.devnull.boteco.domain.MessageProcessor;

import java.util.List;

/**
 * A default strategy to process messages.
 * <p>
 * This strategy checks all {@link MessageProcessor message processors}
 * and the first one capable of process the {@link IncomeMessage message} will
 * be used.
 */
public class BotecoMessageProcessor implements Processor {

  private List<MessageProcessor> messageProcessors;

  public BotecoMessageProcessor(List<MessageProcessor> messageProcessors) {
    this.messageProcessors = messageProcessors;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    IncomeMessage message = exchange.getIn().getBody(IncomeMessage.class);
    messageProcessors.stream()
        .filter(processor -> processor.canProcess(message))
        .findFirst()
        .ifPresent(processor -> processor.process(message));
  }

}