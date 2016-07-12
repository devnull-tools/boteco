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

package tools.devnull.boteco.process.event;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.ServiceLocator;
import tools.devnull.boteco.event.Event;
import tools.devnull.boteco.event.EventListener;

import java.util.List;

public class ListenerFinderProcessor implements Processor {

  public static String EVENT = "event";

  private static final Logger logger = LoggerFactory.getLogger(ListenerFinderProcessor.class);

  private final ServiceLocator serviceLocator;

  public ListenerFinderProcessor(ServiceLocator serviceLocator) {
    this.serviceLocator = serviceLocator;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    Event event = exchange.getIn().getBody(Event.class);
    logger.info(event.object().message());
    List<EventListener> listeners = this.serviceLocator
        .locateAll(EventListener.class, "(|(event=all)(event=%s))", event.id());
    exchange.getOut().setBody(listeners);
    exchange.getOut().setHeader(EVENT, event);
  }

}