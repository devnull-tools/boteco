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

import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessingException;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.user.User;
import tools.devnull.boteco.user.UserManager;

import static tools.devnull.boteco.Predicates.command;
import static tools.devnull.boteco.message.MessageChecker.check;

/**
 * A message processor for user operations.
 */
public class UserMessageProcessor implements MessageProcessor {

  private final UserManager userManager;
  private final UserRepository repository;

  public UserMessageProcessor(UserManager userManager, UserRepository repository) {
    this.userManager = userManager;
    this.repository = repository;
  }

  @Override
  public String id() {
    return "user";
  }

  @Override
  public boolean canProcess(IncomeMessage message) {
    return check(message).accept(command("user"));
  }

  @Override
  public void process(IncomeMessage message) {
    message.command()
        .on("new", String.class, userId -> {
          userManager.create(userId, message.destination());
          message.reply("User created");
        })
        .on("link", BotecoDestinationRequest.class, request -> {
          userManager.link(request);
          message.reply("Link requested and will be effective after confirmation.");
        })
        .on("unlink", BotecoDestinationRequest.class, request -> {
          if (request.targetDestination().channel().equals(message.channel().id())) {
            User user = message.user();
            if (user == null) {
              throw new MessageProcessingException("You're not registered.");
            }
            user.removeDestination(request.targetDestination());
            message.reply("Destination unlinked.");
          } else {
            userManager.unlink(request);
            message.reply("Unlink requested and will be effective after confirmation.");
          }
        })
        .on("default", String.class, channel -> {
          User user = message.user();
          if (user == null) {
            throw new MessageProcessingException("You're not registered.");
          }
          if (channel.isEmpty()) {
            channel = message.channel().id();
          }
          user.setPrimaryDestination(channel);
          this.repository.update(user);
          message.reply("Your new primary destination was saved!");
        })
        .execute();
  }

}
