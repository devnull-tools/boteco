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

import tools.devnull.boteco.client.jms.JmsDestination;

/**
 * An utility class to create destinations.
 */
public class Destination {

  /**
   * Creates a queue destination based on the given queue name.
   *
   * @param name the queue name
   * @return a queue destination
   */
  public static JmsDestination queue(String name) {
    return session -> session.createQueue(name);
  }

  /**
   * Creates a topic destination based on the given topic name.
   *
   * @param name the topic name
   * @return a topic destination
   */
  public static JmsDestination topic(String name) {
    return session -> session.createTopic(name);
  }

  /**
   * Creates a message destination based on the given channel id and target.
   *
   * @param channelId the channel id of the destination
   * @return a component to select the target of the destination
   */
  public static TargetResultSelector<String, MessageDestination> channel(String channelId) {
    return target -> new MessageDestination() {

      @Override
      public int hashCode() {
        return (channelId + target).hashCode();
      }

      @Override
      public boolean equals(Object obj) {
        if (obj instanceof MessageDestination) {
          return channelId.equals(((MessageDestination) obj).channel())
              && target.equals(((MessageDestination) obj).target());
        }
        return false;
      }

      @Override
      public String channel() {
        return channelId;
      }

      @Override
      public String target() {
        return target;
      }
    };
  }

}