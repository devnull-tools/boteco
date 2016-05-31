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

package tools.devnull.boteco.plugins.ping;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.kodo.TestScenario;

import static tools.devnull.boteco.test.IncomeMessageConsumers.reply;
import static tools.devnull.boteco.test.IncomeMessageMock.message;
import static tools.devnull.boteco.test.Predicates.accept;
import static tools.devnull.boteco.test.Predicates.notAccept;
import static tools.devnull.boteco.test.Predicates.receive;
import static tools.devnull.kodo.Spec.should;

public class PingProcessorTest {

  private IncomeMessage pingMessage;
  private IncomeMessage pongMessage;

  @Before
  public void initialize() {
    pingMessage = message("ping", message -> message
        .sentBy("someone")
        .withCommand("ping"));

    pongMessage = message("pong", message -> message
        .sentBy("someone")
        .withCommand("pong"));
  }

  @Test
  public void testAcceptance() {
    TestScenario.given(new PingMessageProcessor())
        .it(should(accept(pingMessage)))
        .it(should(notAccept(pongMessage)))
        // only accepts messages with command
        .it(should(notAccept(message("ping"))));
  }

  @Test
  public void testProcessing() {
    TestScenario.given(new PingMessageProcessor())
        .when(processor -> processor.process(pingMessage))
        .the(pingMessage, should(receive(reply("pong"))));
  }

}
