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
import tools.devnull.boteco.user.User;
import tools.devnull.boteco.user.UserManager;
import tools.devnull.boteco.user.UserNotFoundException;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageSender;

public class BotecoUserManager implements UserManager {

  private final UserRepository repository;
  private final MessageSender messageSender;

  public BotecoUserManager(UserRepository repository, MessageSender messageSender) {
    this.repository = repository;
    this.messageSender = messageSender;
  }

  @Override
  public User find(MessageDestination destination) {
    return repository.find(destination);
  }

  @Override
  public User find(String userId) {
    return repository.find(userId);
  }

  @Override
  public User create(String userId, IncomeMessage message) {
    return repository.create(userId, message.destination());
  }

  @Override
  public User link(String userId, IncomeMessage message) {
    User user = find(userId);
    if (user == null) {
      return create(userId, message);
    }
    user.addDestination(message.destination());
    update(user);
    return user;
  }

  @Override
  public User unlink(String userId, IncomeMessage message) {
    User user = find(userId);
    if (user == null) {
      throw new UserNotFoundException("Couldn't find user with the id " + userId);
    }
    user.removeDestination(message.destination());
    update(user);
    return user;
  }

  @Override
  public void update(User user) {
    this.repository.update(user);
  }

}
