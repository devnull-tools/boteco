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

package tools.devnull.boteco.channel.email;

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.message.Sendable;
import tools.devnull.boteco.user.User;
import tools.devnull.boteco.message.CommandExtractor;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageCommand;
import tools.devnull.boteco.message.MessageSender;
import tools.devnull.boteco.message.Sender;
import tools.devnull.boteco.message.SimpleCommandExtractor;

/**
 * An abstraction for an email income message
 */
public class EmailIncomeMessage implements IncomeMessage {

  private static final long serialVersionUID = 8180521354784182482L;

  private final ServiceRegistry serviceRegistry;
  private final User user;
  private final CommandExtractor extractor;
  private final Channel channel = new EmailChannel();
  private final String content;
  private final Sender sender;
  private final String target;

  /**
   * Creates a new message using the given parameters
   *
   * @param serviceRegistry the service locator for fetching a {@link MessageSender} component
   * @param content         the content of the email
   * @param sender          the sender of the email
   * @param target          the target of the email
   * @param user            the user associated with this message (may be {@code null})
   */
  public EmailIncomeMessage(ServiceRegistry serviceRegistry,
                            String content,
                            EmailSender sender,
                            String target,
                            User user) {
    this.content = content;
    this.sender = sender;
    this.target = target;
    this.serviceRegistry = serviceRegistry;
    this.user = user;
    this.extractor = new SimpleCommandExtractor();
  }

  @Override
  public User user() {
    return this.user;
  }

  @Override
  public Channel channel() {
    return this.channel;
  }

  @Override
  public String content() {
    return this.content;
  }

  @Override
  public Sender sender() {
    return this.sender;
  }

  @Override
  public String target() {
    return this.target;
  }

  @Override
  public boolean isPrivate() {
    return true;
  }

  @Override
  public boolean isGroup() {
    return false;
  }

  @Override
  public boolean hasCommand() {
    return this.extractor.isCommand(this);
  }

  @Override
  public MessageCommand command() {
    return this.extractor.extract(this);
  }

  @Override
  public void reply(Sendable object) {
    this.serviceRegistry.locate(MessageSender.class).one()
        .send(content)
        .with("Subject", object.title() != null ? object.title() : "Re: " + this.content)
        .to(sender().id())
        .through(channel().id());
  }

  @Override
  public void sendBack(Sendable object) {
    reply(content);
  }

}
