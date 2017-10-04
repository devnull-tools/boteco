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

import tools.devnull.boteco.Name;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Name(TimeBombPlugin.ID)
@Command("timebomb")
public class TimeBombMessageProcessor implements MessageProcessor {

  private final BombBag bag;
  private final ScheduledExecutorService executorService;

  public TimeBombMessageProcessor(BombBag bag) {
    this.bag = bag;
    this.executorService = Executors.newScheduledThreadPool(5);
  }

  @Override
  public void process(IncomeMessage message) {
    String target = message.command().as(String.class);
    TimeBomb timebomb = new TimeBomb(new Code(generate()),
        60,
        10
    );
    bag.plant(timebomb, message.location());
    timebomb.onTick(ticks -> {
      if (ticks <= 5) {
        message.sendBack(String.format("Come on! %d seconds left!", ticks));
      }
    });
    timebomb.onMiss(message::sendBack);
    timebomb.onDefuse(code -> message.sendBack("Congrats! The bomb was defused!"));
    timebomb.onBlow(code -> message.group().and(group -> group.kick(target, "Bummer, the code was " + code.value())));
    executorService.scheduleAtFixedRate(timebomb::tick, 0, 1, TimeUnit.SECONDS);
    message.reply("The bomb has been planted!");
  }

  private String generate() {
    List<Integer> digits = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
    Collections.shuffle(digits);
    return digits.subList(0, 4)
        .stream()
        .map(Object::toString)
        .collect(Collectors.joining(""));
  }

}
