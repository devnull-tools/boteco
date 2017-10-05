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

import tools.devnull.boteco.Sendable;

public class Feedback implements Sendable {

  private final String code;
  private final String guess;
  private String feedback;
  private final int attempts;

  public Feedback(String code, String guess, int attempts) {
    this.code = code;
    this.guess = guess;
    this.attempts = attempts;
    initialize();
  }

  private void initialize() {
    StringBuilder feedback = new StringBuilder();
    feedback.append(code).append(" > ");
    for (int i = 0; i < code.length(); i++) {
      if (code.charAt(i) == guess.charAt(i)) {
        feedback.append("✔");
      } else if(code.contains(String.valueOf(guess.charAt(i)))) {
        feedback.append("⚠️");
      } else {
        feedback.append("❌");
      }
    }
    this.feedback = feedback.toString().trim();
  }

  @Override
  public String message() {
    return String.format("%s [%d attempts left]", feedback, attempts);
  }

}
