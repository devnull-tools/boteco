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

package tools.devnull.boteco.channel.telegram;

import tools.devnull.boteco.domain.Channel;
import tools.devnull.boteco.domain.ContentFormatter;
import tools.devnull.boteco.domain.MessageSender;
import tools.devnull.boteco.domain.NullContentFormatter;
import tools.devnull.boteco.domain.TargetSelector;
import tools.devnull.boteco.domain.ServiceLocator;

/**
 * A class that represents the Telegram Channel.
 */
public class TelegramChannel implements Channel, ServiceLocator {

  private static final long serialVersionUID = -774477127136914325L;

  @Override
  public String name() {
    return "Telegram";
  }

  @Override
  public String id() {
    return "telegram";
  }

  @Override
  public ContentFormatter formatter() {
    return new NullContentFormatter();
  }

  @Override
  public TargetSelector send(String content) {
    return target -> locate(MessageSender.class).send(content).to(target).throught(id());
  }

}
