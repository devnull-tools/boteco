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

package tools.devnull.boteco.plugins.request;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.Builder;
import tools.devnull.boteco.MessageLocation;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.message.MessageSender;
import tools.devnull.boteco.message.OutcomeMessageConfiguration;
import tools.devnull.boteco.request.Verifiable;
import tools.devnull.kodo.Spec;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.test.Predicates.receive;
import static tools.devnull.kodo.Expectation.the;
import static tools.devnull.kodo.Expectation.to;

public class RequestManagerTest {

  private RequestRepository repository;
  private MessageSender messageSender;
  private ServiceRegistry serviceRegistry;
  private OutcomeMessageConfiguration configuration;

  @Before
  public void initialize() {
    this.repository = mock(RequestRepository.class);
    this.messageSender = mock(MessageSender.class);
    this.serviceRegistry = mock(ServiceRegistry.class);
    this.configuration = mock(OutcomeMessageConfiguration.class);
  }

  @Test
  public void testCreate() {
    Verifiable verifiable = mock(Verifiable.class);
    MessageLocation destination = mock(MessageLocation.class);
    when(verifiable.tokenDestination()).thenReturn(destination);
    when(messageSender.send(any(Builder.class))).thenReturn(configuration);

    String type = "test";
    String description = "Lorem ipsum cillum nulla sit esse sed";

    Spec.given(new BotecoRequestManager(repository, messageSender, serviceRegistry))
        .when(m -> m.create(verifiable, type, description))

        .expect(the(repository), to(receive(r -> r.create(verifiable, type))))
        .expect(the(configuration), to(receive(c -> c.to(destination))))
        .expect(the(verifiable), to(receive(Verifiable::tokenDestination)));

  }

}
