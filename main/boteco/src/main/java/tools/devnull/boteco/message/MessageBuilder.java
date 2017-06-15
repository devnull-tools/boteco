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

package tools.devnull.boteco.message;

import tools.devnull.boteco.Builder;
import tools.devnull.boteco.Sendable;
import tools.devnull.boteco.SendableObject;

/**
 * A class used to configure a message.
 */
public class MessageBuilder implements Builder<Sendable> {

  private final String content;
  private Priority priority = Priority.NORMAL;
  private String title;
  private String url;

  private MessageBuilder(String content) {
    this.content = content;
  }

  public MessageBuilder withPriority(Priority priority) {
    this.priority = priority;
    return this;
  }

  public MessageBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public MessageBuilder withUrl(String url) {
    this.url = url;
    return this;
  }

  @Override
  public Sendable build() {
    return new SendableObject(content, title, url, priority);
  }

  public static MessageBuilder message(String content) {
    return new MessageBuilder(content);
  }

}
