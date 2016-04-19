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

package tools.devnull.boteco.predicates;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.Predicates;
import tools.devnull.kodo.TestScenario;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.predicates.TestHelper.accept;
import static tools.devnull.boteco.predicates.TestHelper.notAccept;
import static tools.devnull.kodo.Spec.should;

public class MessagePredicateTest {

  private IncomeMessage privateMessage;
  private IncomeMessage groupMessage;

  @Before
  public void initialize() {
    privateMessage = mock(IncomeMessage.class);
    groupMessage = mock(IncomeMessage.class);

    when(privateMessage.isPrivate()).thenReturn(true);
    when(privateMessage.isGroup()).thenReturn(false);

    when(groupMessage.isPrivate()).thenReturn(false);
    when(groupMessage.isGroup()).thenReturn(true);
  }

  @Test
  public void testGroupPredicate() {
    TestScenario.given(Predicates.groupMessage())
        .it(should(accept(groupMessage)))
        .it(should(notAccept(privateMessage)));
  }

  @Test
  public void testPrivatePredicate() {
    TestScenario.given(Predicates.privateMessage())
        .it(should(accept(privateMessage)))
        .it(should(notAccept(groupMessage)));
  }

}
