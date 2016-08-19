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

import org.junit.Test;
import tools.devnull.kodo.TestScenario;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static tools.devnull.kodo.Spec.should;

public class SimpleDiceTest {

  @Test
  public void testDice() {
    for (int i : new int[]{4, 6, 8, 10, 12, 20}) {
      TestScenario.given(new SimpleDice(i))
          .it(should(rollProperly()));
    }
  }

  @Test
  public void testInvalidSides() {
    for (int i : new int[]{1, 2, 3, 5, 7, 9, 11, 13, 14, 15, 16, 17, 18, 19}) {
      TestScenario.given(i)
          .then(SimpleDice::new, should().raise(IllegalArgumentException.class));
    }
  }

  private Predicate<SimpleDice> rollProperly() {
    return dice -> {
      Set<Integer> values = new HashSet<>();
      for (int i = 0; i < 1000; i++) {
        int value = dice.roll();
        if (value < 1 || value > dice.sides()) {
          return false;
        }
        values.add(value);
      }
      return values.size() == dice.sides();
    };
  }

}
