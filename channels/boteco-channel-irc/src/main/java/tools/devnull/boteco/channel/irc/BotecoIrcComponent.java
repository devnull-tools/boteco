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

import org.apache.camel.component.irc.IrcComponent;
import org.apache.camel.component.irc.IrcConfiguration;
import org.schwering.irc.lib.IRCConnection;
import tools.devnull.boteco.event.EventBus;

/**
 * An extension of the IrcComponent that can use custom listeners
 */
public class BotecoIrcComponent extends IrcComponent {

  private final IrcChannelsRepository repository;
  private final EventBus bus;

  public BotecoIrcComponent(IrcChannelsRepository repository, EventBus bus) {
    this.repository = repository;
    this.bus = bus;
  }

  @Override
  protected IRCConnection createConnection(IrcConfiguration configuration) {
    IRCConnection connection = super.createConnection(configuration);
    connection.addIRCEventListener(new BotecoIrcEventListener(connection, configuration, repository, bus));
    return connection;
  }

}
