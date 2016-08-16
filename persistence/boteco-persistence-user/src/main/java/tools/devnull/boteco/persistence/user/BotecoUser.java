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
import tools.devnull.boteco.MessageDestination;
import tools.devnull.boteco.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BotecoUser implements User {

  @SerializedName("_id")
  private final String id;

  @SerializedName("default_destination")
  private final String defaultDestination;

  private final Map<String, String> destinations;

  public BotecoUser(String id, String defaultDestination, Map<String, String> destinations) {
    this.id = id;
    this.defaultDestination = defaultDestination;
    this.destinations = destinations;
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
  public MessageDestination defaultDestination() {
    return Destination.channel(this.defaultDestination).to(this.destinations.get(this.defaultDestination));
  }

}
