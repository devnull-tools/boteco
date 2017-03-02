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

package tools.devnull.boteco.plugins.activation;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.AlwaysActive;
import tools.devnull.boteco.InvocationRule;
import tools.devnull.boteco.MessageLocation;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.plugins.activation.spi.PluginManager;
import tools.devnull.kodo.Spec;

import java.util.function.Predicate;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.test.IncomeMessageMock.message;
import static tools.devnull.kodo.Expectation.because;
import static tools.devnull.kodo.Expectation.it;
import static tools.devnull.kodo.Expectation.to;

public class ActivationRuleTest {

  private PluginManager manager;
  private MessageLocation forbiddenLocation;
  private MessageProcessor forbiddenProcessor;
  private MessageProcessor allowedProcessor;
  private MessageProcessor alwaysActiveProcessor;
  private IncomeMessage messageFromForbiddenLocation;

  private String forbiddenProcessorName;
  private String forbiddenChannel;
  private String forbiddenTarget;

  @Before
  public void initialize() {
    manager = mock(PluginManager.class);
    forbiddenLocation = mock(MessageLocation.class);
    forbiddenProcessor = mock(MessageProcessor.class);
    allowedProcessor = mock(MessageProcessor.class);

    forbiddenProcessorName = "processor";
    forbiddenChannel = "channel";
    forbiddenTarget = "target";

    messageFromForbiddenLocation = message("test", m -> m.from(forbiddenLocation));
    alwaysActiveProcessor = new AlwaysActiveMessageProcessor();

    when(forbiddenProcessor.name()).thenReturn(forbiddenProcessorName);

    when(forbiddenLocation.target()).thenReturn(forbiddenTarget);
    when(forbiddenLocation.channel()).thenReturn(forbiddenChannel);

    when(allowedProcessor.name()).thenReturn("some-other-name");

    when(manager.isEnabled(any(), any())).thenAnswer(
        invocation -> !forbiddenProcessorName.equals(invocation.getArgument(0))
            || forbiddenLocation != invocation.getArgument(1)
    );
  }

  @Test
  public void testScenarios() {
    Spec.given(new ActivationRule(manager))
        .expect(it(), to(approve(messageFromForbiddenLocation, alwaysActiveProcessor)),
            because("Processors with @AlwaysActive annotation cannot be part of a deactivation"))
        .expect(it(), to().not(approve(messageFromForbiddenLocation, forbiddenProcessor)),
            because("Both location and processors are part of a deactivation"))
        .expect(it(), to(approve(messageFromForbiddenLocation, allowedProcessor)),
            because("Both location and processor needs to be part of a deactivation"))
        .expect(it(), to(approve(message("some message"), forbiddenProcessor)),
            because("Both location and processor needs to be part of a deactivation"))
        .expect(it(), to(approve(message("some message"), allowedProcessor)),
            because("Both location and processor are not part of a deactivation"));
  }

  private Predicate<InvocationRule> approve(IncomeMessage message, MessageProcessor processor) {
    return rule -> rule.accept(processor, message);
  }

  @AlwaysActive
  class AlwaysActiveMessageProcessor implements MessageProcessor {

    @Override
    public String name() {
      return forbiddenProcessorName;
    }

    @Override
    public void process(IncomeMessage message) {

    }
  }

}