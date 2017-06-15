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

package tools.devnull.boteco.util;

import org.junit.Test;
import tools.devnull.kodo.Spec;

import java.time.LocalDate;
import java.util.Date;
import java.util.function.Function;

import static tools.devnull.kodo.Expectation.doing;
import static tools.devnull.kodo.Expectation.to;

public class ParameterBinderTest {

  @Test
  public void testStringBind() {
    Spec.given(new ParameterBinder<>(String.class))
        .expect(binding("some string"), to().be("some string"))
        .expect(doing(binding("")), to().raise(IllegalArgumentException.class));
  }

  @Test
  public void testDateBind() {
    Spec.given(new ParameterBinder<>(Date.class))
        .expect(binding("2017-02-04"), to().equal(new Date(2017 - 1900, 1, 4)))
        .expect(doing(binding("")), to().raise(IllegalArgumentException.class))
        .expect(doing(binding("string")), to().raise(IllegalArgumentException.class));
  }

  @Test
  public void testLocalDateBind() {
    Spec.given(new ParameterBinder<>(LocalDate.class))
        .expect(binding("2017-02-04"), to().equal(LocalDate.of(2017, 2, 4)))
        .expect(doing(binding("")), to().raise(IllegalArgumentException.class))
        .expect(doing(binding("string")), to().raise(IllegalArgumentException.class));
  }

  @Test
  public void testIntegerBind() {
    Spec.given(new ParameterBinder<>(Integer.class))
        .expect(binding("10"), to().be(10))
        .expect(doing(binding("")), to().raise(IllegalArgumentException.class))
        .expect(doing(binding("string")), to().raise(IllegalArgumentException.class));

    Spec.given(new ParameterBinder<>(int.class))
        .expect(binding("10"), to().be(10))
        .expect(doing(binding("")), to().raise(IllegalArgumentException.class))
        .expect(doing(binding("string")), to().raise(IllegalArgumentException.class));
  }

  @Test
  public void testLongBind() {
    Spec.given(new ParameterBinder<>(Long.class))
        .expect(binding("10000000000"), to().be(10000000000L))
        .expect(doing(binding("")), to().raise(IllegalArgumentException.class))
        .expect(doing(binding("string")), to().raise(IllegalArgumentException.class));

    Spec.given(new ParameterBinder<>(long.class))
        .expect(binding("10000000000"), to().be(10000000000L))
        .expect(doing(binding("")), to().raise(IllegalArgumentException.class))
        .expect(doing(binding("string")), to().raise(IllegalArgumentException.class));
  }

  private <E> Function<ParameterBinder<E>, E> binding(String content) {
    return binder -> binder.apply(content);
  }

}
