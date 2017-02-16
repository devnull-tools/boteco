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
import tools.devnull.boteco.message.checker.Channel;
import tools.devnull.boteco.message.checker.ChannelChecker;
import tools.devnull.kodo.Spec;

import java.lang.annotation.Annotation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.TestHelper.process;
import static tools.devnull.kodo.Expectation.it;
import static tools.devnull.kodo.Expectation.to;

public class ChannelCheckerTest {

  @Test
  public void testWithSingleChannel() {
    Spec.given(new ChannelChecker(channels("channel1")))
        .expect(it(), to(process(messageFrom("channel1"))))
        .expect(it(), to().not(process(messageFrom("channel2"))));
  }

  @Test
  public void testWithMultipleChannels() {
    Spec.given(new ChannelChecker(channels("channel1", "channel2")))
        .expect(it(), to(process(messageFrom("channel1"))))
        .expect(it(), to(process(messageFrom("channel2"))))
        .expect(it(), to().not(process(messageFrom("channel3"))));
  }

  private IncomeMessage messageFrom(String channelId) {
    IncomeMessage message = mock(IncomeMessage.class);
    tools.devnull.boteco.Channel channel = mock(tools.devnull.boteco.Channel.class);
    when(channel.id()).thenReturn(channelId);
    when(message.channel()).thenReturn(channel);
    return message;
  }

  private Channel channels(String... channels) {
    return new Channel() {

      @Override
      public Class<? extends Annotation> annotationType() {
        return Channel.class;
      }

      @Override
      public String[] value() {
        return channels;
      }
    };
  }

}
