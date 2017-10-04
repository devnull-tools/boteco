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

import tools.devnull.boteco.MessageLocation;
import tools.devnull.boteco.message.MessageSender;

import static tools.devnull.boteco.Sendable.message;

public class Timebomb implements Runnable {

  private final MessageSender messageSender;
  private final MessageLocation location;
  private final String target;
  private final Code code;
  private int time;
  private int attempts;
  private boolean defused;
  private boolean run;

  public Timebomb(MessageSender messageSender,
                  MessageLocation location,
                  String target, Code code,
                  int time,
                  int attempts) {
    this.messageSender = messageSender;
    this.location = location;
    this.target = target;
    this.code = code;
    this.time = time;
    this.attempts = attempts;
    this.defused = false;
    this.run = true;
  }

  public MessageLocation location() {
    return this.location;
  }

  public String target() {
    return this.target;
  }

  public boolean defuse(String value) {
    attempts--;
    if (code.isCorrect(value)) {
      defused = true;
      run = false;
    } else {
      if (attempts == 0) {
        run = false;
      } else {
        messageSender.send(code.getFeedback(value)).to(location);
      }
    }
    return defused;
  }

  @Override
  public void run() {
    while (run) {
      if (time <= 5) {
        messageSender.send(message(String.format("Come on! Just %d seconds!", time))).to(location);
      }
      time--;
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (time == 0) {
        run = false;
      }
    }
    if (defused) {
      messageSender.send(message("Great! You've defused the bomb!")).to(location);
    } else {
      messageSender.send(message(String.format("Bummer!!! The code was %s", code.value()))).to(location);
    }
  }

}
