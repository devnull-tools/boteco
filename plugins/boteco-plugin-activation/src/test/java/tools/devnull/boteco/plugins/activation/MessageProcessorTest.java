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
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import tools.devnull.boteco.MessageLocation;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.plugins.activation.spi.MessageProcessorActivationManager;
import tools.devnull.kodo.Spec;

import java.util.function.Consumer;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.test.IncomeMessageConsumers.reply;
import static tools.devnull.boteco.test.IncomeMessageMock.message;
import static tools.devnull.boteco.test.Predicates.receive;
import static tools.devnull.kodo.Expectation.it;
import static tools.devnull.kodo.Expectation.the;
import static tools.devnull.kodo.Expectation.to;

@RunWith(MockitoJUnitRunner.class)
public class MessageProcessorTest {

  @Mock(answer = Answers.RETURNS_MOCKS)
  private MessageProcessorActivationManager activationManager;

  @Mock
  private MessageLocation location;

  private String processorName = "processor";

  private MessageProcessor activatorMessageProcessor;
  private MessageProcessor deactivatorMessageProcessor;
  private MessageProcessor activationCheckMessageProcessor;


  @Before
  public void initialize() {
    activatorMessageProcessor = new ActivatorMessageProcessor(activationManager);
    deactivatorMessageProcessor = new DeactivatorMessageProcessor(activationManager);
    activationCheckMessageProcessor = new ActivationCheckMessageProcessor(activationManager);

    when(activationManager.isActive(anyString(), eq(location))).thenAnswer(
        i -> processorName.equals(i.getArgumentAt(0, String.class))
    );
  }

  @Test
  public void testActivate() {
    Spec.given(message(m -> m.withCommand("activate", processorName).from(location)))
        .expect(deactivatorMessageProcessor::canProcess, to().be(false))
        .expect(activatorMessageProcessor::canProcess)
        .when(activatorMessageProcessor::process)
        .expect(it(), to(receive(reply("Activation successful!"))))
        .expect(the(activationManager), to(receive(activate())));
  }

  @Test
  public void testDeactivate() {
    Spec.given(message(m -> m.withCommand("deactivate", processorName).from(location)))
        .expect(activatorMessageProcessor::canProcess, to().be(false))
        .expect(deactivatorMessageProcessor::canProcess)
        .when(deactivatorMessageProcessor::process)
        .expect(it(), to(receive(reply("Deactivation successful!"))))
        .expect(the(activationManager), to(receive(deactivate())));
  }

  @Test
  public void testQuery() {
    Spec.given(message(m -> m.withCommand("active?", processorName).from(location)))
        .expect(activationCheckMessageProcessor::canProcess)
        .when(activationCheckMessageProcessor::process)
        .expect(it(), to(receive(reply("[p]Yes[/p]"))))
        .expect(the(activationManager), to(receive(isActive(processorName))));

    Spec.given(message(m -> m.withCommand("active?", "other-processor").from(location)))
        .expect(activationCheckMessageProcessor::canProcess)
        .when(activationCheckMessageProcessor::process)
        .expect(it(), to(receive(reply("[n]No[/n]"))))
        .expect(the(activationManager), to(receive(isActive("other-processor"))));
  }

  private Consumer<MessageProcessorActivationManager> isActive(String processorName) {
    return a -> a.isActive(processorName, location);
  }

  private Consumer<MessageProcessorActivationManager> activate() {
    return a -> a.activate("processor", location);
  }

  private Consumer<MessageProcessorActivationManager> deactivate() {
    return a -> a.deactivate("processor", location);
  }


}
