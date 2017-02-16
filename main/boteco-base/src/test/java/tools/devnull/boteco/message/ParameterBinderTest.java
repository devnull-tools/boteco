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
import tools.devnull.boteco.util.ParameterBinder;
import tools.devnull.kodo.Spec;

import java.util.function.Consumer;
import java.util.function.Function;

import static tools.devnull.kodo.Expectation.to;

public class ParameterBinderTest {

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

    private Integer x;
    private int y;

    public TestObject2(Integer x, int y) {
      this.x = x;
      this.y = y;
    }

    public int y() {
      return y;
    }

    public Integer x() {
      return x;
    }

  }

  @Test
  public void testParameterValues() {
    ParameterBinder<TestObject1> converter = new ParameterBinder<>(TestObject1.class);

    Spec.given(converter)
        .expect(converting("value1 value2"), to().succeed())
        .expect(converting("value"), to().raise(IllegalArgumentException.class))
        .expect(converting("value1 value2 value3"), to().raise(IllegalArgumentException.class));

    Spec.given(converter.apply("value1 value2"))
        .expect(TestObject1::foo, to().be("value1"))
        .expect(TestObject1::bar, to().be("value2"));

    Spec.given(converter.apply("value1 \"value2 is a text\""))
        .expect(TestObject1::foo, to().be("value1"))
        .expect(TestObject1::bar, to().be("value2 is a text"));

    Spec.given(converter.apply("value1 \"value2 \\\"is a\\\" text\""))
        .expect(TestObject1::foo, to().be("value1"))
        .expect(TestObject1::bar, to().be("value2 \"is a\" text"));
  }

  @Test
  public void testNumericParameterValues() {
    ParameterBinder<TestObject2> converter = new ParameterBinder<>(TestObject2.class);

    Spec.given(converter)
        .expect(converting("1 2"), to().succeed())
        .expect(converting("1 foo"), to().raise(IllegalArgumentException.class))
        .expect(converting("1 2 3"), to().raise(IllegalArgumentException.class));

    Spec.given(converter.apply("1 3"))
        .expect(TestObject2::x, to().be(1))
        .expect(TestObject2::y, to().be(3));
  }

  private Consumer<Function> converting(String value) {
    return converter -> converter.apply(value);
  }

}
