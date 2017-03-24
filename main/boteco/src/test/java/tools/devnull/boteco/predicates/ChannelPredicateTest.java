/*
 * The MIT License
 *
 * Copyright (c) 2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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
import tools.devnull.boteco.Channel;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.Predicates;
import tools.devnull.kodo.Spec;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.TestHelper.accept;
import static tools.devnull.boteco.TestHelper.notAccept;
import static tools.devnull.kodo.Expectation.it;
import static tools.devnull.kodo.Expectation.to;

public class ChannelPredicateTest {

  private IncomeMessage messageFromUnknownChannel;
  private IncomeMessage messageFromOtherChannel;
  private IncomeMessage messageFromMyChannel;

  private Channel myChannel = newChannel("my-channel");
  private Channel otherChannel = newChannel("other-channel");
  private Channel unknownChannel = newChannel("unknown-channel");

  private Channel newChannel(String id) {
    Channel channel = mock(Channel.class);
    when(channel.id()).thenReturn(id);
    return channel;
  }

  @Before
  public void initialize() {
    messageFromMyChannel = mock(IncomeMessage.class);
    when(messageFromMyChannel.channel()).thenReturn(myChannel);

    messageFromOtherChannel = mock(IncomeMessage.class);
    when(messageFromOtherChannel.channel()).thenReturn(otherChannel);

    messageFromUnknownChannel = mock(IncomeMessage.class);
    when(messageFromUnknownChannel.channel()).thenReturn(unknownChannel);
  }

  @Test
  public void test() {
    Spec.given(Predicates.channel("my-channel"))
        .expect(it(), to(accept(messageFromMyChannel)))
        .expect(it(), to(notAccept(messageFromOtherChannel)))
        .expect(it(), to(notAccept(messageFromUnknownChannel)));
  }

}
