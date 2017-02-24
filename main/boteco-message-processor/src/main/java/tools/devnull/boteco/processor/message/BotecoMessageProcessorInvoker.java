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

package tools.devnull.boteco.processor.message;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.DomainException;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.event.EventBus;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessingError;
import tools.devnull.boteco.message.MessageProcessingException;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.InvocationRule;

/**
 * A processor that invokes a {@link MessageProcessor} to process
 * a specific message.
 */
public class BotecoMessageProcessorInvoker implements Processor {

  private static final Logger logger = LoggerFactory.getLogger(BotecoMessageProcessorInvoker.class);

  private final EventBus eventBus;
  private final ServiceRegistry serviceRegistry;

  /**
   * Creates a new processor
   *
   * @param eventBus        the event bus to broadcast error events
   * @param serviceRegistry the service registry to query for rules
   */
  public BotecoMessageProcessorInvoker(EventBus eventBus, ServiceRegistry serviceRegistry) {
    this.eventBus = eventBus;
    this.serviceRegistry = serviceRegistry;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    Message income = exchange.getIn();
    MessageProcessor messageProcessor = income.getBody(MessageProcessor.class);
    IncomeMessage message = income.getHeader(BotecoMessageProcessorFinder.INCOME_MESSAGE, IncomeMessage.class);

    if (serviceRegistry.locate(InvocationRule.class).all().stream()
        .allMatch(rule -> rule.accept(messageProcessor, message))) {
      try {
        messageProcessor.process(message);
      } catch (MessageProcessingException | DomainException e) {
        eventBus.broadcast(new MessageProcessingError(message, e)).as("error.message-processor");
      } catch (Throwable e) {
        logger.error(e.getMessage(), e);
      }
    }
  }

}