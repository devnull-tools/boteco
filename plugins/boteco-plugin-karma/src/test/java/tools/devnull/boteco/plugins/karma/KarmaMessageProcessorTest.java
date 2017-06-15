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
import tools.devnull.kodo.Spec;

import java.util.Properties;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.test.Consumers.process;
import static tools.devnull.kodo.Expectation.the;
import static tools.devnull.kodo.Expectation.to;

public class KarmaMessageProcessorTest {

  private KarmaRepository repository;
  private Karma testKarma;
  private Karma otherKarma;

  @Before
  public void initialize() {
    testKarma = new Karma("test");
    otherKarma = new Karma("other");

    repository = mock(KarmaRepository.class);
    when(repository.find("test")).thenReturn(testKarma);
    when(repository.find("other")).thenReturn(otherKarma);
  }

  @Test
  public void testRaise() {
    Spec.given(new KarmaMessageProcessor(repository, new Properties()))
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
    Spec.given(new KarmaMessageProcessor(repository, new Properties()))
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
    Spec.given(new KarmaMessageProcessor(repository, new Properties()))
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

}
