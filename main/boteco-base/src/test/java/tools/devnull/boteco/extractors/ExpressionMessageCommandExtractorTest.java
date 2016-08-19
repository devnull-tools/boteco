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
import tools.devnull.kodo.TestScenario;

import java.util.function.Predicate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.kodo.Spec.should;

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
    extractor = new ExpressionCommandExtractor("^command\\s");
    command = extractor.extract(message("command foo arg0 arg1"));
  }

  @Test
  public void testCheck() {
    TestScenario.given(extractor)
        .it(should(accept("command foo")))
        .it(should(accept("Command Foo")))
        .it(should(reject("not a command foo")));
  }

  @Test
  public void testExtraction() {
    TestScenario.given(extractor.extract(message("command foo")))
        .then(MessageCommand::name, should().be("foo"));

    TestScenario.given(extractor.extract(message("command bar")))
        .then(MessageCommand::name, should().be("bar"));

    TestScenario.given(command)
        .then(MessageCommand::name, should().be("foo"))
        .then(command -> command.as(String.class), should().be("arg0 arg1"));
  }

  private Predicate<CommandExtractor> accept(String string) {
    return commandExtractor -> commandExtractor.isCommand(message(string));
  }

  private Predicate<CommandExtractor> reject(String string) {
    return accept(string).negate();
  }

}
