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

package tools.devnull.boteco.message;

import java.util.function.Predicate;

/**
 * An utility class to help building {@link MessageProcessor#canProcess(IncomeMessage)}
 * methods.
 *
 * @see #check(IncomeMessage)
 * @see MessageProcessor
 */
public class MessageChecker {

  private final IncomeMessage message;

  private MessageChecker(IncomeMessage message) {
    this.message = message;
  }

  /**
   * Accepts messages that are accepted by the given predicate.
   *
   * @param predicate the predicate to test the message
   * @return {@code true} if the message is accepted by the given predicate
   */
  public boolean accept(Predicate<IncomeMessage> predicate) {
    return predicate.test(message);
  }

  /**
   * Accepts messages that are rejected by the given predicate.
   *
   * @param predicate the predicate to test the message
   * @return {@code true} if the message is rejected by the given predicate
   */
  public boolean reject(Predicate<IncomeMessage> predicate) {
    return !predicate.test(message);
  }

  /**
   * Checks the given message.
   *
   * @param message the message to check
   * @return a MessageChecker to proceed with the DSL
   */
  public static MessageChecker check(IncomeMessage message) {
    return new MessageChecker(message);
  }

}
