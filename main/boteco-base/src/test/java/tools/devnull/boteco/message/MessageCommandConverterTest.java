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

package tools.devnull.boteco.message;

import org.junit.Test;
import tools.devnull.kodo.TestScenario;

import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.kodo.Spec.should;

public class MessageCommandConverterTest {

  public static class TestObject1 {

    private String foo;
    private String bar;

    public TestObject1(String foo, String bar) {

      this.foo = foo;
      this.bar = bar;
    }

    public String foo() {
      return foo;
    }

    public String bar() {
      return bar;
    }

  }

  public static class TestObject2 {

    private String foo;

    public TestObject2(String foo) {
      this.foo = foo;
    }

    public TestObject2(IncomeMessage message) {
      this.foo = message.content();
    }

    public String foo() {
      return foo;
    }

  }

  @Test
  public void testParameterValues() {
    IncomeMessage message = mock(IncomeMessage.class);
    MessageCommandConverter<TestObject1> converter = new MessageCommandConverter<>(message, TestObject1.class);

    TestScenario.given(converter)
        .then(converting("value1 value2"), should().succeed())
        .then(converting("value"), should().raise(MessageProcessingException.class));

    TestScenario.given(converter.apply("value1 value2"))
        .then(TestObject1::foo, should().be("value1"))
        .then(TestObject1::bar, should().be("value2"));
  }

  @Test
  public void testConstructorMatch() {
    IncomeMessage message = mock(IncomeMessage.class);
    when(message.content()).thenReturn("content");

    MessageCommandConverter<TestObject2> converter = new MessageCommandConverter<>(message, TestObject2.class);

    TestScenario.given(converter)
        .then(converting("value"), should().succeed())
        .then(converting(""), should().succeed());

    TestScenario.given(converter.apply(""))
        .then(TestObject2::foo, should().be("content"));

    TestScenario.given(converter.apply("bar"))
        .then(TestObject2::foo, should().be("bar"));
  }

  private Consumer<MessageCommandConverter> converting(String value) {
    return converter -> converter.apply(value);
  }

}
