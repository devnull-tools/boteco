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

import tools.devnull.boteco.MessageDestination;
import tools.devnull.boteco.Param;
import tools.devnull.boteco.Parameters;
import tools.devnull.boteco.message.IncomeMessage;

/**
 * A class that represents a request to link a destination
 * to an user.
 */
@Parameters({
    "user channel target",
    "user"
})
public class LinkRequest {

  private final String user;
  private final String channel;
  private final String target;

  public LinkRequest(@Param("user") String user,
                     @Param("channel") String channel,
                     @Param("target") String target) {
    this.user = user;
    this.channel = channel;
    this.target = target;
  }

  public LinkRequest(String user, IncomeMessage message) {
    MessageDestination destination = message.destination();
    this.user = user;
    this.channel = destination.channel();
    this.target = destination.target();
  }

  public String user() {
    return user;
  }

  public String channel() {
    return channel;
  }

  public String target() {
    return target;
  }

}
