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
import tools.devnull.boteco.MessageLocation;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.plugins.user.spi.UserRepository;
import tools.devnull.boteco.request.RequestManager;
import tools.devnull.boteco.user.PrimaryDestinationRequest;
import tools.devnull.boteco.user.User;
import tools.devnull.boteco.user.UserManager;

public class BotecoUserManager implements UserManager {

  private final UserRepository repository;
  private final RequestManager requestManager;

  public BotecoUserManager(UserRepository repository, RequestManager requestManager) {
    this.repository = repository;
    this.requestManager = requestManager;
  }

  @Override
  public User find(MessageLocation destination) {
    return repository.find(destination);
  }

  @Override
  public User find(String userId) {
    return repository.find(userId);
  }

  @Override
  public User create(String userId, MessageLocation destination) {
    return repository.create(userId, destination);
  }

  @Override
  public void link(IncomeMessage message, String userId, String channel, String target) {
    UserRequest request = message.user()
        .filter(user -> user.id().equals(userId))
        .map(user -> new UserRequest(userId, channel, target, Destination.channel(channel).to(target)))
        .orElseReturn(() -> new UserRequest(userId, channel, target, Destination.channel("user").to(userId)));
    this.requestManager.create(request, "user.link", "link account to user " + userId);
  }

  @Override
  public void unlink(String userId, String channel) {
    this.requestManager.create(new UserRequest(userId, channel),
        "user.unlink", "unlink account from user " + userId);
  }

  @Override
  public void changePrimaryDestination(PrimaryDestinationRequest request) {
    this.requestManager.create(request, "user.primaryDestination",
        "primary destination to " + request.channel() + " for user " + request.userId());
  }

}
