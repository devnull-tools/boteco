/*
 * The MIT License
 *
 * Copyright (c) 2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.boteco.processor.message;

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.Message;
import tools.devnull.boteco.message.MessageCommand;
import tools.devnull.boteco.message.MessageSender;
import tools.devnull.boteco.Sendable;
import tools.devnull.boteco.message.Sender;
import tools.devnull.boteco.user.User;

import javax.validation.Validator;

public class BotecoIncomeMessage implements IncomeMessage {

  private final ServiceRegistry registry;
  private final Message message;

  public BotecoIncomeMessage(ServiceRegistry registry, Message message) {
    this.registry = registry;
    this.message = message;
  }

  @Override
  public Channel channel() {
    return message.channel();
  }

  @Override
  public String content() {
    return message.content();
  }

  @Override
  public Sender sender() {
    return message.sender();
  }

  @Override
  public User user() {
    return message.user();
  }

  @Override
  public String target() {
    return message.target();
  }

  @Override
  public boolean isPrivate() {
    return message.isPrivate();
  }

  @Override
  public boolean isGroup() {
    return message.isGroup();
  }

  @Override
  public String replyId() {
    return message.replyId();
  }

  @Override
  public boolean hasCommand() {
    return channel().commandExtractor().isCommand(this);
  }

  @Override
  public MessageCommand command() {
    return new ValidatableMessageCommand(registry.locate(Validator.class).one(),
        channel().commandExtractor().extract(this));
  }

  @Override
  public void reply(Sendable object) {
    getMessageSender()
        .send(object)
        .replyingTo(isPrivate() ? null : replyId())
        .to(target())
        .through(channel().id());
  }

  @Override
  public void sendBack(Sendable object) {
    getMessageSender()
        .send(object)
        .to(target())
        .through(channel().id());
  }

  private MessageSender getMessageSender() {
    return registry.locate(MessageSender.class).one();
  }

}
