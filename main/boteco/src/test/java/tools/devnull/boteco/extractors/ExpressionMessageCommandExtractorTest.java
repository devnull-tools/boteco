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

package tools.devnull.boteco.extractors;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.message.CommandExtractor;
import tools.devnull.boteco.message.ExpressionCommandExtractor;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageCommand;
import tools.devnull.kodo.Spec;

import java.util.function.Predicate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.kodo.Expectation.it;
import static tools.devnull.kodo.Expectation.to;

public class ExpressionMessageCommandExtractorTest {

  private CommandExtractor extractor;
  private MessageCommand command;

  private IncomeMessage message(String content) {
    IncomeMessage message = mock(IncomeMessage.class);
    when(message.content()).thenReturn(content);
    return message;
  }

  @Before
  public void initialize() {
    extractor = new ExpressionCommandExtractor("^command\\s(?<command>\\S+)\\s*(?<arguments>.+)?");
    command = extractor.extract(message("command foo arg0 arg1"));
  }

  @Test
  public void testCheck() {
    Spec.given(extractor)
        .expect(it(), to(accept("command foo")))
        .expect(it(), to(accept("Command Foo")))
        .expect(it(), to(reject("not a command foo")));
  }

  @Test
  public void testExtraction() {
    Spec.given(extractor.extract(message("command foo")))
        .expect(MessageCommand::name, to().be("foo"));

    Spec.given(extractor.extract(message("command bar")))
        .expect(MessageCommand::name, to().be("bar"));

    Spec.given(command)
        .expect(MessageCommand::name, to().be("foo"))
        .expect(command -> command.as(String.class), to().be("arg0 arg1"));
  }

  private Predicate<CommandExtractor> accept(String string) {
    return commandExtractor -> commandExtractor.isCommand(message(string));
  }

  private Predicate<CommandExtractor> reject(String string) {
    return accept(string).negate();
  }

}
