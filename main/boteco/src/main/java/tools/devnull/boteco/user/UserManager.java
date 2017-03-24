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

package tools.devnull.boteco.user;

import tools.devnull.boteco.MessageLocation;
import tools.devnull.boteco.message.IncomeMessage;

/**
 * Interface that defines a component capable of managing users.
 */
public interface UserManager {

  /**
   * Finds the user that had registered the given destination.
   *
   * @param destination the user destination
   * @return the user that has the given destination linked to it
   */
  User find(MessageLocation destination);

  /**
   * Finds the user by its ID
   *
   * @param userId the user id
   * @return the user that has the given ID
   */
  User find(String userId);

  /**
   * Creates and registers a user based on the given ID and primary destination
   *
   * @param userId             the user id
   * @param primaryDestination the user's primary destination
   * @return the created user
   * @throws UserAlreadyExistException if an user with this id is already registered
   */
  User create(String userId, MessageLocation primaryDestination) throws UserAlreadyExistException;

  /**
   * Links the given destination to a user.
   * <p>
   * This operation might create a request for confirmation.
   *
   * @param message the message that originates the request for link
   * @param userId  the user id
   * @param channel the channel of the destination
   * @param target  the target of the destination
   */
  void link(IncomeMessage message, String userId, String channel, String target);

  /**
   * Removes the destination from the user's destinations.
   * <p>
   * This operation might create a request for confirmation.
   *
   * @param userId  the user id
   * @param channel the channel of the destination
   */
  void unlink(String userId, String channel);

  /**
   * Changes a user's primary destination
   * <p>
   * This operation might create a request for confirmation.
   *
   * @param request the request
   */
  void changePrimaryDestination(PrimaryDestinationRequest request);

}
