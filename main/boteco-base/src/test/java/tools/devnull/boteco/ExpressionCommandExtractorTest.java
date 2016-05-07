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

package tools.devnull.boteco;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.extractors.ExpressionCommandExtractor;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.kodo.TestScenario;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.kodo.Spec.EMPTY;
import static tools.devnull.kodo.Spec.be;
import static tools.devnull.kodo.Spec.should;

public class ExpressionCommandExtractorTest {

  private CommandExtractor extractor;
  private Command command;

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
        .it(should(reject("not a command foo")));
  }

  @Test
  public void testExtraction() {
    TestScenario.given(extractor.extract(message("command foo")))
        .the(name, should(be("foo")))
        .the(args, should(be(EMPTY)));

    TestScenario.given(extractor.extract(message("command bar")))
        .the(name, should(be("bar")))
        .the(args, should(be(EMPTY)));

    TestScenario.given(command)
        .the(name, should(be("foo")))
        .the(arg, should(be("arg0 arg1")));
  }

  @Test
  public void testArgumentList() {
    TestScenario.given(command.args())
        .the(List::size, should(be(2)))
        .the(item(0), should(be("arg0")))
        .the(item(1), should(be("arg1")));
  }

  private Function<Command, String> name = command -> command.name();
  private Function<Command, List<String>> args = command -> command.args();
  private Function<Command, String> arg = command -> command.arg();

  private Function<List<String>, Object> item(int index) {
    return l -> l.get(index);
  }

  private Predicate<CommandExtractor> accept(String string) {
    return commandExtractor -> commandExtractor.isCommand(message(string));
  }

  private Predicate<CommandExtractor> reject(String string) {
    return accept(string).negate();
  }

}
