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
import tools.devnull.boteco.MessageLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BombBag {

  private final Map<String, TimeBomb> bombs;
  private final ScheduledExecutorService executorService;

  public BombBag() {
    this.bombs = new ConcurrentHashMap<>();
    this.executorService = Executors.newScheduledThreadPool(5);
  }

  public void plant(TimeBomb timebomb, MessageLocation location) {
    String key = getKey(location);
    if (bombs.containsKey(key)) {
      throw new DomainException("There is already a bomb planted here");
    }
    bombs.put(key, timebomb);
    timebomb.onBlow(s -> bombs.remove(key));
    timebomb.onDefuse(s -> bombs.remove(key));
    executorService.scheduleAtFixedRate(timebomb::tick, 0, 1, TimeUnit.SECONDS);
  }

  public TimeBomb bombFor(MessageLocation location) {
    return bombs.get(getKey(location));
  }

  private String getKey(MessageLocation location) {
    return String.format("%s:%s", location.channel(), location.target());
  }

}
