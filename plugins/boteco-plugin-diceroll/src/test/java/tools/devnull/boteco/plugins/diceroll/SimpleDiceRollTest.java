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

package tools.devnull.boteco.plugins.diceroll;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.kodo.TestScenario;

import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tools.devnull.kodo.Spec.should;

public class SimpleDiceRollTest {

  private Function<Integer, Dice> function;

  @Before
  public void initialize() {
    function = mock(Function.class);
    Dice d4 = mock(Dice.class);
    when(d4.roll()).thenReturn(4);
    when(function.apply(4)).thenReturn(d4);

    Dice d6 = mock(Dice.class);
    when(d6.roll()).thenReturn(6);
    when(function.apply(6)).thenReturn(d6);
  }

  @Test
  public void testExpressions() {
    TestScenario.given(new SimpleDiceRoll(function))
        .then(resultOf("d4"), should().be(4))
        .then(resultOf("2d4"), should().be(8))
        .then(resultOf("3d4"), should().be(12))
        .then(resultOf("d4 + 3"), should().be(7))

        .then(resultOf("d6"), should().be(6))
        .then(resultOf("2d6"), should().be(12))
        .then(resultOf("3d6"), should().be(18))
        .then(resultOf("d6 + 3"), should().be(9))
        .then(resultOf("d4 + d6"), should().be(10))
        .then(resultOf("d4 + d6 + 1"), should().be(11));
  }

  private Function<SimpleDiceRoll, Integer> resultOf(String expression) {
    return diceRoll -> diceRoll.roll(expression);
  }

}
