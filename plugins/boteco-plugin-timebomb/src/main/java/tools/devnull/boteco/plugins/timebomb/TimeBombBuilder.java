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

package tools.devnull.boteco.plugins.timebomb;

import tools.devnull.boteco.DomainException;
import tools.devnull.boteco.message.IncomeMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TimeBombBuilder {

  private final IncomeMessage message;
  private final String target;
  private final int time;
  private final int length;

  public TimeBombBuilder(IncomeMessage message, String target, int time, int length) {
    if ((length <= 0 || length > 10) || time < 0) {
      throw new DomainException("Couldn't set up a bomb with the provided parameters");
    }
    this.message = message;
    this.target = target;
    this.time = time;
    this.length = length;
  }

  public TimeBombBuilder(IncomeMessage message, String target) {
    this(message, target, 60, 5);
  }

  public TimeBombBuilder(IncomeMessage message, int time, int length) {
    this(message, null, time, length);
  }

  public TimeBomb build() {
    TimeBomb timebomb = new TimeBomb(generate(length), time, 10);
    if (target != null) {
      timebomb.onBlow(code -> message.group()
          .and(group -> group.kick(target, "Bummer! The code was " + code)));
    }
    timebomb.onTick(ticks -> {
      if (ticks == 5) {
        message.sendBack("Come on!!!! It's gonna blow!!!");
      }
    });
    timebomb.onMiss(message::sendBack);
    timebomb.onDefuse(code -> message.sendBack("Congrats! The bomb was defused!"));
    timebomb.onBlow(code -> message.sendBack("Bummer! The code was " + code));
    return timebomb;
  }

  private String generate(int length) {
    List<Integer> digits = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    Collections.shuffle(digits);
    return digits.subList(0, length)
        .stream()
        .map(Object::toString)
        .collect(Collectors.joining(""));
  }

}
