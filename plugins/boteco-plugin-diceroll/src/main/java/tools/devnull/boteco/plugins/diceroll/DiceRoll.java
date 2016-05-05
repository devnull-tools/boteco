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

import java.util.Arrays;

public class DiceRoll {

  private final String roll;

  public DiceRoll(String roll) {
    this.roll = roll;
  }

  public int roll() {
    String[] rolls = roll.split("\\s*[+]\\s*");
    if (rolls.length > 1) {
      return Arrays.stream(rolls)
          .mapToInt(s -> new DiceRoll(s).roll())
          .reduce(0, (left, right) -> left + right);
    } else {
      return _roll();
    }
  }

  private int _roll() {
    int total = 0;
    int dices = 0;
    int sides = 0;

    if (roll.startsWith("d")) {
      dices = 1;
      sides = Integer.parseInt(roll.substring(1));
    } else if(roll.matches("\\d+")) {
      total = Integer.parseInt(roll);
    } else {
      String[] split = roll.split("d");
      dices = Integer.parseInt(split[0]);
      sides = Integer.parseInt(split[1]);
    }

    for (int i = 0; i < dices; i++) {
      total += new Dice(sides).roll();
    }

    return total;
  }

}
