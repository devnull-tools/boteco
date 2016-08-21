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
import tools.devnull.boteco.Param;
import tools.devnull.boteco.Parameters;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.user.DestinationRequest;

/**
 * A class that represents a request to link/unlink a destination
 * to an user.
 */
@Parameters({
    "channel target",
    "user"
})
public class BotecoDestinationRequest implements DestinationRequest {

  private final String user;
  private final MessageDestination targetDestination;
  private final MessageDestination tokenDestination;

  public BotecoDestinationRequest(IncomeMessage message,
                                  @Param("channel") String channel,
                                  @Param("target") String target) {
    this.user = message.user().id();
    this.targetDestination = Destination.channel(channel).to(target);
    this.tokenDestination = targetDestination;
  }

  public BotecoDestinationRequest(@Param("user") String user, IncomeMessage message) {
    this.user = user;
    this.targetDestination = message.destination();
    this.tokenDestination = Destination.channel("user").to(user);
  }

  public String userId() {
    return user;
  }

  public MessageDestination targetDestination() {
    return targetDestination;
  }

  public MessageDestination tokenDestination() {
    return tokenDestination;
  }

}
