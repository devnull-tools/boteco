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

package tools.devnull.boteco.channel.irc;

import tools.devnull.boteco.AlwaysActive;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;

import java.util.stream.Collectors;

/**
 * A message processor that can adjust how the bot behaves on the IRC.
 */
@Command("irc")
@AlwaysActive
public class IrcMessageProcessor implements MessageProcessor {

  private final IrcIgnoreList ignoreList;

  /**
   * Creates a new processor using the given ignore list
   *
   * @param ignoreList the ignore list to use
   */
  public IrcMessageProcessor(IrcIgnoreList ignoreList) {
    this.ignoreList = ignoreList;
  }

  @Override
  public void process(IncomeMessage message) {
    message.command()
        .on("ignore", nickname -> {
          this.ignoreList.add(nickname);
          message.reply("Added to ignore list");
        })
        .on("accept", nickname -> {
          this.ignoreList.remove(nickname);
          message.reply("Removed from ignore list");
        })
        .on("ignored", () -> message.reply(this.ignoreList.list().stream().collect(Collectors.joining("\n"))))
        .execute();
  }

}
