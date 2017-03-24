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

package tools.devnull.boteco.plugins.user;

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.Destination;
import tools.devnull.boteco.MessageLocation;
import tools.devnull.boteco.user.PrimaryDestinationRequest;
import tools.devnull.boteco.user.User;

public class BotecoPrimaryDestinationRequest implements PrimaryDestinationRequest {

  private final String user;
  private final String channel;

  public BotecoPrimaryDestinationRequest(String user, String channel) {
    this.user = user;
    this.channel = channel;
  }

  public BotecoPrimaryDestinationRequest(User user, Channel channel) {
    this.user = user.id();
    this.channel = channel.id();
  }

  public BotecoPrimaryDestinationRequest(User user, String channel) {
    this.channel = channel;
    this.user = user.id();
  }

  public String userId() {
    return user;
  }

  public String channel() {
    return channel;
  }

  @Override
  public MessageLocation tokenDestination() {
    return Destination.channel("user").to(user);
  }

}
