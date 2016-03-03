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

package tools.devnull.boteco.domain.predicates;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.domain.IncomeMessage;
import tools.devnull.kodo.TestScenario;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.kodo.Spec.should;

import static tools.devnull.boteco.domain.predicates.TestHelper.*;

public class ChannelPredicateTest {

  private IncomeMessage messageFromUnknownChannel;
  private IncomeMessage messageFromOtherChannel;
  private IncomeMessage messageFromMyChannel;

  @Before
  public void initialize() {
    messageFromMyChannel = mock(IncomeMessage.class);
    when(messageFromMyChannel.channel()).thenReturn("my-channel");

    messageFromOtherChannel = mock(IncomeMessage.class);
    when(messageFromOtherChannel.channel()).thenReturn("other-channel");

    messageFromUnknownChannel = mock(IncomeMessage.class);
    when(messageFromUnknownChannel.channel()).thenReturn("unknown-channel");
  }

  @Test
  public void test() {
    TestScenario.given(new ChannelPredicate("my-channel", "other-channel"))
        .it(should(accept(messageFromMyChannel)))
        .it(should(accept(messageFromOtherChannel)))
        .it(should(notAccept(messageFromUnknownChannel)));
  }

}
