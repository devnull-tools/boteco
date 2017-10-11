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

import tools.devnull.boteco.message.CommandExtractor;
import tools.devnull.boteco.message.ExpressionCommandExtractor;
import tools.devnull.boteco.message.Message;
import tools.devnull.boteco.message.MessageCommand;
import tools.devnull.trugger.Optional;

import java.io.Serializable;

/**
 * A command extractor for Telegram bots.
 */
public class TelegramCommandExtractor implements CommandExtractor, Serializable {

  private static final long serialVersionUID = -1777063166866821628L;

  private final CommandExtractor extractor;

  public TelegramCommandExtractor(String botUsername) {
    this.extractor = new ExpressionCommandExtractor("^/(?<command>[^@ ]*)(@" + botUsername +
        ")?(?<arguments>\\s+.+)?$");
  }

  @Override
  public Optional<MessageCommand> extract(Message message) {
    return extractor.extract(message);
  }

}
