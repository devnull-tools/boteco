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
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.event.Event;
import tools.devnull.boteco.event.EventListener;

import java.util.List;

import static tools.devnull.boteco.Predicates.eq;
import static tools.devnull.boteco.Predicates.serviceProperty;

/**
 * A processor that finds listeners for events raised
 */
public class ListenerFinderProcessor implements Processor {

  public static String EVENT = "event";

  private static final Logger logger = LoggerFactory.getLogger(ListenerFinderProcessor.class);

  private final ServiceRegistry serviceRegistry;

  /**
   * Creates a new processor using the given service locator for
   * fetching the {@link EventListener listeners}
   *
   * @param serviceRegistry the service locator to use
   */
  public ListenerFinderProcessor(ServiceRegistry serviceRegistry) {
    this.serviceRegistry = serviceRegistry;
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    Event event = exchange.getIn().getBody(Event.class);
    List<EventListener> listeners = this.serviceRegistry
        .locate(EventListener.class)
        .filter(serviceProperty("event", eq("all").or(eq(event.id()))))
        .all();
    logger.info("Found " + listeners.size() + " listeners for event " + event.id());
    exchange.getOut().setBody(listeners);
    exchange.getOut().setHeader(EVENT, event);
  }

}
