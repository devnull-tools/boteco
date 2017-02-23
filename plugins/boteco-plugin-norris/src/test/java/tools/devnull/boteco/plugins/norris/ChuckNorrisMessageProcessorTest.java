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

package tools.devnull.boteco.plugins.norris;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.client.rest.RestClient;
import tools.devnull.boteco.client.rest.RestConfiguration;
import tools.devnull.boteco.client.rest.RestResult;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.kodo.Spec;

import java.util.function.Consumer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.test.IncomeMessageMock.message;
import static tools.devnull.boteco.test.Predicates.receive;
import static tools.devnull.kodo.Expectation.it;
import static tools.devnull.kodo.Expectation.to;

public class ChuckNorrisMessageProcessorTest {

  private MessageProcessor messageProcessor;
  private ChuckNorrisFact fact = new ChuckNorrisFact("Lorem ipsum non sit id magna occaecat.");

  @Before
  public void initialize() throws Exception {
    RestClient restClient = mock(RestClient.class);
    RestConfiguration restConfiguration = mock(RestConfiguration.class);
    RestResult restResult = mock(RestResult.class);

    when(restClient.get("https://api.chucknorris.io/jokes/random")).thenReturn(restConfiguration);
    when(restConfiguration.to(ChuckNorrisFact.class)).thenReturn(restResult);
    when(restResult.and(any(Consumer.class))).thenAnswer(invocation -> {
      ((Consumer<ChuckNorrisFact>) invocation.getArguments()[0]).accept(fact);
      return null;
    });

    messageProcessor = new ChuckNorrisMessageProcessor(restClient);
  }

  @Test
  public void testValidMessages() {
    Spec.given(message("lorem chuck norris ipsum"))
        .expect(messageProcessor::canProcess, to().be(true))

        .when(messageProcessor::process)
        .expect(it(), to(receive(sendBack())));

    Spec.given(message("lorem Chuck Norris ipsum"))
        .expect(messageProcessor::canProcess, to().be(true))

        .when(messageProcessor::process)
        .expect(it(), to(receive(sendBack())));

    Spec.given(message("lorem chucknorris ipsum"))
        .expect(messageProcessor::canProcess, to().be(true))

        .when(messageProcessor::process)
        .expect(it(), to(receive(sendBack())));

    Spec.given(message("lorem #chucknorris ipsum"))
        .expect(messageProcessor::canProcess, to().be(true))

        .when(messageProcessor::process)
        .expect(it(), to(receive(sendBack())));
  }

  @Test
  public void testInvalidMessages() {
    Spec.given(message("lorem chuck ipsum"))
        .expect(messageProcessor::canProcess, to().be(false));

    Spec.given(message("lorem norris ipsum"))
        .expect(messageProcessor::canProcess, to().be(false));
  }

  private Consumer<IncomeMessage> sendBack() {
    return message -> message.sendBack(fact);
  }

}
