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

package tools.devnull.boteco;

/**
 * A default implementation of a Message Destination
 */
public class UserMessageDestination implements MessageDestination {

  private final String channel;
  private final String target;

  public UserMessageDestination(String channel, String target) {
    this.channel = channel;
    this.target = target;
  }

  @Override
  public int hashCode() {
    return (channel + target).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MessageDestination) {
      return channel.equals(((MessageDestination) obj).channel())
          && target.equals(((MessageDestination) obj).target());
    }
    return false;
  }

  @Override
  public String channel() {
    return channel;
  }

  @Override
  public String target() {
    return target;
  }

  public static UserMessageDestination of(MessageDestination destination) {
    return new UserMessageDestination(destination.channel(), destination.target());
  }

}
