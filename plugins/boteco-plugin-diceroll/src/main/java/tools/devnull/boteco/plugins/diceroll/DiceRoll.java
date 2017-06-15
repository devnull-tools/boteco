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

/**
 * Interface that represents a dice roll.
 * <p>
 * A dice roll may use a set of different dices and also has a fixed
 * increment to the total value.
 */
public interface DiceRoll {

  /**
   * Rolls the dices specified by the given expression.
   * <p>
   * A dice roll is represented by the following expression:
   * <p>
   * {@code n}d{@code s} (+ {@code n}d{@code s})* (+ {@code v})?
   * <p>
   * Where 'n' is the number of dices (optional in case of '1'), 's' is the number
   * of sides and 'v' is a static increment to the final value.
   * <p>
   * Example: 2d4 + d8 + 3 will roll 2 dices of 4 sides, one dice of 8 sides and add
   * 3 to the final score.
   *
   * @param expression the expression that defines the roll
   * @return the final score
   */
  int roll(String expression);

}
