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

package tools.devnull.boteco.plugins.manager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import tools.devnull.boteco.MessageLocation;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.plugins.manager.spi.PluginManager;
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
public class ManagerMessageProcessorTest {

  @Mock(answer = Answers.RETURNS_MOCKS)
  private PluginManager activationManager;

  @Mock
  private MessageLocation location;

  private String processorName = "processor";

  private MessageProcessor messageProcessor;


  @Before
  public void initialize() {
    messageProcessor = new PluginManagerMessageProcessor(activationManager);

    when(activationManager.isEnabled(anyString(), eq(location))).thenAnswer(
        i -> processorName.equals(i.getArgument(0))
    );
  }

  @Test
  public void testEnable() {
    Spec.given(message(m -> m.withCommand("plugin", "enable", processorName).from(location)))
        .expect(messageProcessor::canProcess)
        .when(messageProcessor::process)
        .expect(it(), to(receive(reply("Plugin(s) enabled successfully!"))))
        .expect(the(activationManager), to(receive(enable())));
  }

  @Test
  public void testDeactivate() {
    Spec.given(message(m -> m.withCommand("plugin", "disable", processorName).from(location)))
        .expect(messageProcessor::canProcess)
        .when(messageProcessor::process)
        .expect(it(), to(receive(reply("Plugin(s) disabled successfully!"))))
        .expect(the(activationManager), to(receive(disable())));
  }

  @Test
  public void testQuery() {
    Spec.given(message(m -> m.withCommand("plugin", "enabled", processorName).from(location)))
        .expect(messageProcessor::canProcess)
        .when(messageProcessor::process)
        .expect(it(), to(receive(reply("[p]Yes, the plugin is enabled for this channel[/p]"))))
        .expect(the(activationManager), to(receive(isActive(processorName))));

    Spec.given(message(m -> m.withCommand("plugin", "enabled", "other-processor").from(location)))
        .expect(messageProcessor::canProcess)
        .when(messageProcessor::process)
        .expect(it(), to(receive(reply("[n]No, the plugin is disabled for this channel[/n]"))))
        .expect(the(activationManager), to(receive(isActive("other-processor"))));
  }

  private Consumer<PluginManager> isActive(String processorName) {
    return a -> a.isEnabled(processorName, location);
  }

  private Consumer<PluginManager> enable() {
    return a -> a.enable("processor", location);
  }

  private Consumer<PluginManager> disable() {
    return a -> a.disable("processor", location);
  }


}
