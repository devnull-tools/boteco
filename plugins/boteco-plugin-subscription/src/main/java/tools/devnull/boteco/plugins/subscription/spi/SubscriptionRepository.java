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

package tools.devnull.boteco.plugins.subscription.spi;

import tools.devnull.boteco.event.Subscription;

import java.util.List;

/**
 * Interface that defines a repository for subscriptions
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public interface SubscriptionRepository {

  /**
   * Finds all subscriptions for a given event id
   *
   * @param eventId the event to search
   * @return a list of all subscriptions for that event
   */
  List<Subscription> find(String eventId);

  /**
   * Finds all subscriptions that notifies the given message endpoint
   *
   * @param channel the channel of the target
   * @param target  the target that will be notified
   * @return all subscriptions for the given endpoint
   */
  List<Subscription> find(String channel, String target);

  /**
   * Inserts a subscription using the given information.
   *
   * @param eventId the event id
   * @param channel the channel
   * @param target  the target
   */
  void insert(String eventId, String channel, String target);

  /**
   * Removes a subscription using the given information.
   *
   * @param eventId the event id
   * @param channel the channel
   * @param target  the target
   */
  void delete(String eventId, String channel, String target);

}
