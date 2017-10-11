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

import org.junit.Test;
import tools.devnull.boteco.message.CommandExtractor;
import tools.devnull.boteco.message.MessageCommand;
import tools.devnull.kodo.Spec;
import tools.devnull.trugger.Optional;

import static tools.devnull.boteco.test.IncomeMessageMock.message;
import static tools.devnull.kodo.Expectation.to;

public class TelegramCommandExtractorTest {

  private CommandExtractor extractor = new TelegramCommandExtractor("boteco_bot");

  @Test
  public void testSimpleCommand() {
    Spec.given(commandOf("/command arguments"))
        .expect(Optional::exists)

        .given(Optional::value)
        .expect(MessageCommand::name, to().be("command"))
        .expect(MessageCommand::asString, to().be("arguments"))

        .given(commandOf("/command"))
        .expect(Optional::exists)

        .given(Optional::value)
        .expect(MessageCommand::name, to().be("command"))

        .given(commandOf("not a command"))
        .expect(Optional::exists, to().be(false));
  }

  @Test
  public void testDirectCommand() {
    Spec.given(commandOf("/command@boteco_bot"))
        .expect(Optional::exists)

        .given(Optional::value)
        .expect(MessageCommand::name, to().be("command"))
        .expect(MessageCommand::asString, to().be(""))

        .given(commandOf("/command@boteco_bot arguments"))
        .expect(Optional::exists)

        .given(Optional::value)
        .expect(MessageCommand::name, to().be("command"))
        .expect(MessageCommand::asString, to().be("arguments"))

        .given(commandOf("/command@another_bot"))
        .expect(Optional::exists, to().be(false));
  }

  private Optional<MessageCommand> commandOf(String content) {
    return extractor.extract(message(content));
  }

}
