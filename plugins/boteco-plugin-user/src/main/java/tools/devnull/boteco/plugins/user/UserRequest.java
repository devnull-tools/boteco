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

package tools.devnull.boteco.plugins.user;

import tools.devnull.boteco.Destination;
import tools.devnull.boteco.MessageDestination;
import tools.devnull.boteco.UserMessageDestination;
import tools.devnull.boteco.request.Verifiable;

public class UserRequest implements Verifiable {

  private final String user;
  private final String channel;
  private final String target;
  private final UserMessageDestination targetDestination;

  public UserRequest(String user, MessageDestination destination, MessageDestination targetDestination) {
    this.user = user;
    this.channel = destination.channel();
    this.target = destination.target();
    this.targetDestination = new UserMessageDestination(targetDestination.channel(), targetDestination.target());
  }

  public String getUser() {
    return user;
  }

  public MessageDestination destination() {
    return Destination.channel(this.channel).to(this.target);
  }

  @Override
  public MessageDestination targetDestination() {
    return targetDestination;
  }

}