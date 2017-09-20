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

import tools.devnull.boteco.MessageLocation;
import tools.devnull.boteco.user.User;

/**
 * Interface that represents a repository of User objects.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public interface UserRepository {

  /**
   * Finds the user associated with the given message location
   *
   * @param location the location
   * @return the user associated with the given location
   */
  User find(MessageLocation location);

  /**
   * Finds the user associated with the given id
   *
   * @param userId the id of the user
   * @return the user associated with the given id
   */
  User find(String userId);

  /**
   * Creates a new user and associates it with the given location
   *
   * @param userId   the user id
   * @param location the message location
   * @return the created user
   */
  User create(String userId, MessageLocation location);

  /**
   * Updates the given user in the repository
   *
   * @param user the user to update
   */
  void update(User user);

}
