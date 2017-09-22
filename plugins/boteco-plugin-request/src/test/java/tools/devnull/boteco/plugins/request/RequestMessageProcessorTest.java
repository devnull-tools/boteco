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

package tools.devnull.boteco.plugins.request;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.request.RequestManager;
import tools.devnull.boteco.test.IncomeMessageMock;
import tools.devnull.kodo.Spec;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.test.Consumers.process;
import static tools.devnull.boteco.test.Predicates.receive;
import static tools.devnull.kodo.Expectation.the;
import static tools.devnull.kodo.Expectation.to;

public class RequestMessageProcessorTest {

  private RequestManager requestManager;

  private String validToken = "validToken";
  private String invalidToken = "invalidToken";

  @Before
  public void initialize() {
    requestManager = mock(RequestManager.class);
    when(requestManager.confirm(validToken)).thenReturn(true);
    when(requestManager.confirm(invalidToken)).thenReturn(false);
  }

  @Test
  public void testValidToken() {
    IncomeMessage message = IncomeMessageMock.message(validToken,
        m -> m.withCommand("confirm", validToken));

    Spec.given(new RequestMessageProcessor(requestManager))
        .when(process(message))
        .expect(the(message), to(receive(m -> m.reply("Confirmation OK!"))));
  }

  @Test
  public void testInvalidToken() {
    IncomeMessage message = IncomeMessageMock.message(invalidToken,
        m -> m.withCommand("confirm", invalidToken));

    Spec.given(new RequestMessageProcessor(requestManager))
        .when(process(message))
        .expect(the(message), to(receive(m -> m.reply("[e]Invalid token[/e]"))));
  }

}
