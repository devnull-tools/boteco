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

package tools.devnull.boteco.message.checkers;

import org.junit.Test;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageCommand;
import tools.devnull.boteco.message.checker.Command;
import tools.devnull.boteco.message.checker.CommandChecker;
import tools.devnull.kodo.TestScenario;

import java.lang.annotation.Annotation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.TestHelper.process;
import static tools.devnull.kodo.Spec.should;

public class CommandCheckerTest {

  @Test
  public void test() {
    TestScenario.given(new CommandChecker(command("command1")))
        .it(should(process(messageWith("command1"))))
        .it(should().not(process(messageWith("command2"))));
  }

  private IncomeMessage messageWith(String commandName) {
    IncomeMessage message = mock(IncomeMessage.class);
    MessageCommand command = mock(MessageCommand.class);
    when(command.name()).thenReturn(commandName);
    when(message.hasCommand()).thenReturn(true);
    when(message.command()).thenReturn(command);
    return message;
  }

  private Command command(String commandName) {
    return new Command() {

      @Override
      public Class<? extends Annotation> annotationType() {
        return Command.class;
      }

      @Override
      public String value() {
        return commandName;
      }
    };
  }

}
