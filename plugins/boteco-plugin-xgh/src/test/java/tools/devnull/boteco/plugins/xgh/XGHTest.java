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

package tools.devnull.boteco.plugins.xgh;

import org.junit.Test;
import tools.devnull.kodo.Spec;

import java.util.function.Function;
import java.util.function.Predicate;

import static tools.devnull.kodo.Expectation.doing;
import static tools.devnull.kodo.Expectation.to;

public class XGHTest {

  @Test
  public void testAxioms() {
    Spec.given(new XGH())
        .expect(doing(xgh -> xgh.axiom(1)), to().succeed())
        .expect(doing(xgh -> xgh.axiom(23)), to().fail())
        .expect(doing(xgh -> xgh.axiom(0)), to().fail());

    for (int i = 1 ; i <= 22 ; i++) {
      Spec.given(new XGH())
          .expect(axiom(i), to().have(number(i)));
    }
  }

  private Predicate<Axiom> number(int i) {
    return axiom -> axiom.getNumber() == i;
  }

  private Function<XGH, Axiom> axiom(int n) {
    return xgh -> xgh.axiom(n);
  }

}
