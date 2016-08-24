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

package tools.devnull.boteco.persistence.user;

import com.google.gson.annotations.SerializedName;
import tools.devnull.boteco.Destination;
import tools.devnull.boteco.InvalidDestinationException;
import tools.devnull.boteco.MessageDestination;
import tools.devnull.boteco.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BotecoUser implements User {

  private static final long serialVersionUID = 7185963234889976203L;

  @SerializedName("_id")
  private final String id;
  private final Map<String, String> destinations;
  private String primaryDestination;

  public BotecoUser(String id, MessageDestination primaryDestination) {
    this.id = id;
    this.primaryDestination = primaryDestination.channel();
    this.destinations = new HashMap<>();
    this.destinations.put(primaryDestination.channel(), primaryDestination.target());
  }

  @Override
  public String id() {
    return this.id;
  }

  @Override
  public List<MessageDestination> destinations() {
    return this.destinations.entrySet().stream()
        .map(entry -> Destination.channel(entry.getKey()).to(entry.getValue()))
        .collect(Collectors.toList());
  }

  @Override
  public MessageDestination primaryDestination() {
    return Destination.channel(this.primaryDestination).to(this.destinations.get(this.primaryDestination));
  }

  @Override
  public MessageDestination destination(String channel) {
    if (this.destinations.containsKey(channel)) {
      return Destination.channel(channel).to(this.destinations.get(channel));
    }
    throw new InvalidDestinationException("User don't have the channel '" + channel + "' registered");
  }

  @Override
  public void addDestination(MessageDestination destination) {
    this.destinations.put(destination.channel(), destination.target());
  }

  @Override
  public void removeDestination(MessageDestination destination) {
    if (destination.channel().equals(this.primaryDestination)) {
      throw new InvalidDestinationException("Can't remove default destination");
    }
    if (this.destinations.containsKey(destination.channel())) {
      this.destinations.remove(destination.channel());
    } else {
      throw new InvalidDestinationException("User don't have the given destination");
    }
  }

  public void setPrimaryDestination(MessageDestination primaryDestination) {
    addDestination(primaryDestination);
    this.primaryDestination = primaryDestination.channel();
  }

  @Override
  public void setPrimaryDestination(String channel) {
    if (this.destinations.containsKey(channel)) {
      this.primaryDestination = channel;
    } else {
      throw new InvalidDestinationException("User don't have the channel " + channel + " linked");
    }
  }

}
