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

package tools.devnull.boteco.channel.irc;

import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;

import java.util.stream.Collectors;

import static tools.devnull.boteco.Predicates.command;
import static tools.devnull.boteco.message.MessageChecker.check;

/**
 * A message processor that can manipulate an {@link IrcIgnoreList}.
 */
public class IgnoreListMessageProcessor implements MessageProcessor {

  private final IrcIgnoreList ignoreList;

  public IgnoreListMessageProcessor(IrcIgnoreList ignoreList) {
    this.ignoreList = ignoreList;
  }

  @Override
  public String id() {
    return "ignore-list";
  }

  @Override
  public boolean canProcess(IncomeMessage message) {
    return check(message).accept(command("ignore-list"));
  }

  @Override
  public void process(IncomeMessage message) {
    message.command()
        .on("add", nickname -> {
          this.ignoreList.add(nickname);
          message.reply("Added to ignore list");
        })
        .on("remove", nickname -> {
          this.ignoreList.remove(nickname);
          message.reply("Removed from ignore list");
        })
        .on("", () -> message.reply(this.ignoreList.list().stream().collect(Collectors.joining("\n"))))
        .execute();
  }

}
