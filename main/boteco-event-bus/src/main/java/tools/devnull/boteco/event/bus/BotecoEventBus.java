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

package tools.devnull.boteco.event.bus;

import tools.devnull.boteco.client.jms.JmsClient;
import tools.devnull.boteco.event.EventBus;
import tools.devnull.boteco.event.EventSelector;
import tools.devnull.boteco.message.Sendable;

import static tools.devnull.boteco.Destination.topic;

/**
 * The default implementation of an event bus that uses JMS topics.
 * <p>
 * Events processed by this component will be delivered to the topic
 * named "boteco.event.$ID", where $ID is the {@link EventSelector#as(String) ID}
 * of the Event.
 */
public class BotecoEventBus implements EventBus {

  private final JmsClient client;

  /**
   * Creates a new bus using the given jms client to send the events.
   *
   * @param client the client to use
   */
  public BotecoEventBus(JmsClient client) {
    this.client = client;
  }

  @Override
  public EventSelector broadcast(Sendable object) {
    return id -> client.send(new BotecoEvent(id, object)).to(topic("boteco.event." + id));
  }

  @Override
  public EventSelector broadcast(String message) {
    return broadcast(new Sendable() {
      private static final long serialVersionUID = 7895898641225035972L;

      @Override
      public String message() {
        return message;
      }

      @Override
      public String title() {
        return null;
      }

      @Override
      public String url() {
        return null;
      }
    });
  }

}
