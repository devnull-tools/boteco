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
  public User find(MessageDestination destination) {
    return repository.find(destination);
  }

  @Override
  public User find(String userId) {
    return repository.find(userId);
  }

  @Override
  public User create(String userId, MessageDestination destination) {
    return repository.create(userId, destination);
  }

  @Override
  public LinkSelector link(MessageDestination destination) {
    return userId -> tokenDestination -> this.requestManager.create(
        new UserRequest(userId, destination, tokenDestination),
        "user.link",
        "link account to user"
    );
  }

  @Override
  public UnlinkSelector unlink(MessageDestination destination) {
    return userId -> tokenDestination -> this.requestManager.create(
        new UserRequest(userId, destination, tokenDestination),
        "user.unlink",
        "unlink account from"
    );
  }

  @Override
  public void requestPrimartDestinationChange(PrimaryDestinationRequest request) {
    this.requestManager.create(request,
        "user.primaryDestination",
        "primary destination to " + request.channel());
  }

}
