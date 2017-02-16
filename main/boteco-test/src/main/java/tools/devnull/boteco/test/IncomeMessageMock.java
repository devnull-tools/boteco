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

package tools.devnull.boteco.test;

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageCommand;
import tools.devnull.boteco.message.Sender;

import java.util.Arrays;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A class to help creating an income message
 */
public class IncomeMessageMock {

  private final IncomeMessage mock;

  private IncomeMessageMock(String message) {
    this.mock = mock(IncomeMessage.class);
    when(mock.content()).thenReturn(message);
  }

  public IncomeMessageMock from(String channelId) {
    Channel channel = mock(Channel.class);
    when(channel.id()).thenReturn(channelId);
    return from(channel);
  }

  public IncomeMessageMock from(Channel channel) {
    when(mock.channel()).thenReturn(channel);
    return this;
  }

  public IncomeMessageMock sentBy(Sender sender) {
    when(mock.sender()).thenReturn(sender);
    return this;
  }

  public IncomeMessageMock sentBy(String senderId) {
    Sender sender = mock(Sender.class);
    when(sender.id()).thenReturn(senderId);
    return sentBy(sender);
  }

  public IncomeMessageMock withCommand(String name, String... args) {
    MessageCommand command = mock(MessageCommand.class);
    when(command.name()).thenReturn(name);
    StringBuilder arg = new StringBuilder();
    Arrays.stream(args).forEach(s -> arg.append(s).append(" "));
    when(command.as(String.class)).thenReturn(arg.toString().trim());
    when(mock.command()).thenReturn(command);
    when(mock.hasCommand()).thenReturn(true);
    return this;
  }

  public static IncomeMessage message(String content) {
    return message(content, mock -> {});
  }

  public static IncomeMessage privateMessage(String content) {
    return privateMessage(content, mock -> {});
  }

  public static IncomeMessage groupMessage(String content) {
    return groupMessage(content, mock -> {});
  }

  public static IncomeMessage message(String content, Consumer<IncomeMessageMock> config) {
    IncomeMessageMock incomeMessageMock = new IncomeMessageMock(content);
    config.accept(incomeMessageMock);
    return incomeMessageMock.mock;
  }

  public static IncomeMessage privateMessage(String content, Consumer<IncomeMessageMock> config) {
    IncomeMessageMock incomeMessageMock = new IncomeMessageMock(content);
    config.accept(incomeMessageMock);
    when(incomeMessageMock.mock.isPrivate()).thenReturn(true);
    return incomeMessageMock.mock;
  }

  public static IncomeMessage groupMessage(String content, Consumer<IncomeMessageMock> config) {
    IncomeMessageMock incomeMessageMock = new IncomeMessageMock(content);
    config.accept(incomeMessageMock);
    when(incomeMessageMock.mock.isGroup()).thenReturn(true);
    return incomeMessageMock.mock;
  }

}
