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

package tools.devnull.boteco.plugins.diceroll;

import java.util.Arrays;

public class SimpleDice implements Dice {

  private final int sides;

  /**
   * Creates a new dice with the given number of sides.
   * <p>
   * Only dices with 4, 6, 8, 10, 12 or 20 sides can be created.
   *
   * @param sides the number of the sides
   * @throws IllegalArgumentException in case of a unexpected number of sides
   */
  public SimpleDice(int sides) {
    if (!Arrays.asList(4, 6, 8, 10, 12, 20).contains(sides)) {
      throw new IllegalArgumentException("I only have these: d4, d6, d8, d10, d12 and d20");
    }
    this.sides = sides;
  }

  @Override
  public int sides() {
    return sides;
  }

  @Override
  public int roll() {
    return (int) (1 + (Math.random() * sides));
  }

}
