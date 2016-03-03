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

package tools.devnull.boteco.plugin.ping;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.domain.Command;
import tools.devnull.boteco.domain.IncomeMessage;
import tools.devnull.boteco.domain.MessageProcessor;
import tools.devnull.kodo.TestScenario;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tools.devnull.kodo.Spec.be;
import static tools.devnull.kodo.Spec.notBe;
import static tools.devnull.kodo.Spec.should;

public class PingProcessorTest {

  private MessageProcessor processor;

  private IncomeMessage pingMessage;
  private IncomeMessage pongMessage;

  private Command pingCommand;
  private Command pongCommand;

  private IncomeMessage messageWithoutCommand;

  @Before
  public void initialize() {
    processor = new PingMessageProcessor();

    pingMessage = mock(IncomeMessage.class);
    when(pingMessage.sender()).thenReturn("someone");
    when(pingMessage.hasCommand()).thenReturn(true);

    pingCommand = mock(Command.class);
    when(pingCommand.name()).thenReturn("ping");

    when(pingMessage.command()).thenReturn(pingCommand);

    pongMessage = mock(IncomeMessage.class);
    when(pongMessage.sender()).thenReturn("someone");
    when(pongMessage.hasCommand()).thenReturn(true);

    pongCommand = mock(Command.class);
    when(pongCommand.name()).thenReturn("pong");

    when(pongMessage.command()).thenReturn(pongCommand);

    messageWithoutCommand = mock(IncomeMessage.class);
    when(messageWithoutCommand.hasCommand()).thenReturn(false);
  }

  @Test
  public void testProcess() {
    TestScenario.given(pingMessage)
        .it(should(be(processable())))

        .when(processed())
        .it(should(be(replied())))
        .the(command(), should(be(tested())));

    TestScenario.given(pongMessage)
        .it(should(notBe(processable())))
        .the(command(), should(be(tested())));
  }

  private Predicate<Command> tested() {
    return c -> {
      verify(c).name();
      return true;
    };
  }

  private Function<IncomeMessage, Command> command() {
    return m -> m.command();
  }

  private Predicate<IncomeMessage> processable() {
    return m -> processor.canProcess(m);
  }

  private Predicate<IncomeMessage> replied() {
    return m -> {
      verify(m).reply("%s: pong", m.sender());
      return true;
    };
  }

  private Consumer<IncomeMessage> processed() {
    return m -> processor.process(m);
  }

}
