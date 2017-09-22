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

package tools.devnull.boteco.plugins.karma;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.kodo.Spec;

import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.test.Consumers.process;
import static tools.devnull.boteco.test.IncomeMessageMock.message;
import static tools.devnull.boteco.test.Predicates.receive;
import static tools.devnull.kodo.Expectation.it;
import static tools.devnull.kodo.Expectation.the;
import static tools.devnull.kodo.Expectation.to;

public class KarmaMessageProcessorTest {

  private KarmaRepository repository;
  private MessageProcessor processor;

  @Before
  public void initialize() {
    repository = mock(KarmaRepository.class);
    processor = new KarmaMessageProcessor(repository);
  }

  @Test
  public void testRaise() {
    Karma testKarma = new Karma("test");
    Karma otherKarma = new Karma("other");

    when(repository.find("test")).thenReturn(testKarma);
    when(repository.find("other")).thenReturn(otherKarma);

    Spec.given(processor)
        .expect(the(testKarma.value()), to().be(0))
        .expect(the(otherKarma.value()), to().be(0))

        .when(process("test++"))
        .expect(the(testKarma.value()), to().be(1))
        .expect(the(otherKarma.value()), to().be(0))

        .when(process("lorem ipsum test++, dolor"))
        .expect(the(testKarma.value()), to().be(2))
        .expect(the(otherKarma.value()), to().be(0))

        .when(process("lorem ipsum test++. dolor"))
        .expect(the(testKarma.value()), to().be(3))
        .expect(the(otherKarma.value()), to().be(0))

        .when(process("lorem ipsum test++! dolor"))
        .expect(the(testKarma.value()), to().be(4))
        .expect(the(otherKarma.value()), to().be(0))

        .when(process("lorem ipsum test++ test++ dolor"))
        .expect(the(testKarma.value()), to().be(5))
        .expect(the(otherKarma.value()), to().be(0));
  }

  @Test
  public void testLower() {
    Karma testKarma = new Karma("test");
    Karma otherKarma = new Karma("other");

    when(repository.find("test")).thenReturn(testKarma);
    when(repository.find("other")).thenReturn(otherKarma);

    Spec.given(processor)
        .expect(the(testKarma.value()), to().be(0))
        .expect(the(otherKarma.value()), to().be(0))

        .when(process("test--"))
        .expect(the(testKarma.value()), to().be(-1))
        .expect(the(otherKarma.value()), to().be(0))

        .when(process("lorem ipsum test--, dolor"))
        .expect(the(testKarma.value()), to().be(-2))
        .expect(the(otherKarma.value()), to().be(0))

        .when(process("lorem ipsum test--. dolor"))
        .expect(the(testKarma.value()), to().be(-3))
        .expect(the(otherKarma.value()), to().be(0))

        .when(process("lorem ipsum test--! dolor"))
        .expect(the(testKarma.value()), to().be(-4))
        .expect(the(otherKarma.value()), to().be(0))

        .when(process("lorem ipsum test-- test-- dolor"))
        .expect(the(testKarma.value()), to().be(-5))
        .expect(the(otherKarma.value()), to().be(0));
  }

  @Test
  public void testMultiple() {
    Karma testKarma = new Karma("test");
    Karma otherKarma = new Karma("other");

    when(repository.find("test")).thenReturn(testKarma);
    when(repository.find("other")).thenReturn(otherKarma);

    Spec.given(new KarmaMessageProcessor(repository))
        .expect(the(testKarma.value()), to().be(0))
        .expect(the(otherKarma.value()), to().be(0))

        .when(process("test-- other++"))
        .expect(the(testKarma.value()), to().be(-1))
        .expect(the(otherKarma.value()), to().be(1))

        .when(process("test-- other++"))
        .expect(the(testKarma.value()), to().be(-2))
        .expect(the(otherKarma.value()), to().be(2))

        .when(process("test-- other--"))
        .expect(the(testKarma.value()), to().be(-3))
        .expect(the(otherKarma.value()), to().be(1))

        .when(process("test++ other++"))
        .expect(the(testKarma.value()), to().be(-2))
        .expect(the(otherKarma.value()), to().be(2))

        .when(process("test++ other--"))
        .expect(the(testKarma.value()), to().be(-1))
        .expect(the(otherKarma.value()), to().be(1))

        .when(process("test++ other--"))
        .expect(the(testKarma.value()), to().be(0))
        .expect(the(otherKarma.value()), to().be(0));
  }

  @Test
  public void testReplyMessages() {
    Karma something = new Karma("something");
    Karma someone = new Karma("someone");

    when(repository.find("something")).thenReturn(something);
    when(repository.find("someone")).thenReturn(someone);

    Spec.given(message("something++"))
        .when(processor::process)
        .expect(it(), to(receive(sendBack("[a]something[/a] has now [p]1[/p] points of karma"))))

    .given(message("someone--"))
        .when(processor::process)
        .expect(it(), to(receive(sendBack("[a]someone[/a] has now [n]-1[/n] points of karma"))))

        .given(message("someone-- something++"))
        .when(processor::process)
        .expect(it(), to(receive(sendBack("[a]someone[/a] has now [n]-2[/n] points of karma\n" +
            "[a]something[/a] has now [p]2[/p] points of karma"))));
  }

  private Consumer<IncomeMessage> sendBack(String content) {
    return message -> message.sendBack(content);
  }

}
