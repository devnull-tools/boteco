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

package tools.devnull.boteco.plugins.diceroll;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.kodo.TestScenario;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.test.Consumers.process;
import static tools.devnull.boteco.test.IncomeMessageConsumers.reply;
import static tools.devnull.boteco.test.IncomeMessageMock.message;
import static tools.devnull.boteco.test.Predicates.accept;
import static tools.devnull.boteco.test.Predicates.notAccept;
import static tools.devnull.boteco.test.Predicates.receive;
import static tools.devnull.kodo.Spec.should;

public class DiceRollMessageProcessorTest {

  private DiceRoll diceRoll;
  private IncomeMessage message;
  private int expectedScore;
  private String sender;

  @Before
  public void initialize() {
    expectedScore = 6;
    sender = "someone";
    diceRoll = mock(DiceRoll.class);
    when(diceRoll.roll("d10")).thenReturn(expectedScore);

    message = message("roll d10", msg -> msg
        .sentBy(sender)
        .withCommand("roll", "d10")
    );
  }

  @Test
  public void testAcceptance() {
    TestScenario.given(new DiceRollMessageProcessor(diceRoll))
        .it(should(notAccept(message("roll", msg -> msg.withCommand("roll")))))
        .it(should(accept(message)));
  }

  @Test
  public void testProcess() {
    TestScenario.given(new DiceRollMessageProcessor(diceRoll))
        .when(process(message))
        .the(message, should(receive(IncomeMessage::command)))
        .the(message, should(receive(IncomeMessage::sender)))
        .the(message, should(receive(reply("[m]%s[/m], you got [v]%s[/v] points!", sender, expectedScore))));
  }

}
