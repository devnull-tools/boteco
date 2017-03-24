/*
 * The MIT License
 *
 * Copyright (c) 2016-2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.boteco.event;

import java.util.List;

/**
 * An interface that defines a manager for event subscription.
 */
public interface SubscriptionManager {

  /**
   * Lists all subscriptions for the given event id.
   *
   * @param eventId the event's id
   * @return a list containing all subscriptions for the given event.
   */
  List<Subscription> subscriptions(String eventId);

  /**
   * Lists all subscriptions for the given channel and target
   *
   * @param channel the channel
   * @param target  the target
   * @return a list containing all subscriptions for the given channel and target
   */
  List<Subscription> subscriptions(String channel, String target);

  /**
   * Creates a subscription. A subscription contains a subscriber (target and channel)
   * and an event id. Any event with the given id broadcasted to the Event Bus will
   * have its message delivered to the subscriber.
   *
   * @return a component to input the subscription information
   */
  SubscriptionTargetSelector subscribe();

  /**
   * Removes a subscription.
   *
   * @return a component to input the subscription information
   */
  SubscriptionRemovalTargetSelector unsubscribe();

}
