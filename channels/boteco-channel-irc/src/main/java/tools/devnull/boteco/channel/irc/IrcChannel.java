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

import org.schwering.irc.lib.IRCConnection;
import tools.devnull.boteco.Channel;
import tools.devnull.boteco.Group;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.message.CommandExtractor;
import tools.devnull.trugger.Optional;

/**
 * Implementation of the IRC Channel
 */
public class IrcChannel implements Channel {

  /**
   * The ID of this channel.
   */
  public static final String ID = "irc";

  private final CommandExtractor commandExtractor;
  private final ServiceRegistry registry;

  public IrcChannel(CommandExtractor commandExtractor, ServiceRegistry registry) {
    this.commandExtractor = commandExtractor;
    this.registry = registry;
  }

  @Override
  public boolean canSend() {
    return true;
  }

  @Override
  public boolean canReceive() {
    return true;
  }

  @Override
  public String name() {
    return "IRC";
  }

  @Override
  public String id() {
    return ID;
  }

  @Override
  public CommandExtractor commandExtractor() {
    return this.commandExtractor;
  }

  @Override
  public Optional<Group> group(String groupId) {
    return registry.providerOf(IRCConnection.class)
        .map(p -> new IrcGroup(p.get(), groupId));
  }

  static class IrcGroup implements Group {

    private final IRCConnection connection;
    private final String channelName;

    IrcGroup(IRCConnection connection, String channelName) {
      this.connection = connection;
      this.channelName = channelName;
    }

    @Override
    public void kick(String user, String reason) {
      if (!connection.getUsername().equals(user)) {
        connection.doKick(channelName, user, reason);
      }
    }

  }

}
