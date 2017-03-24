/*
 * The MIT License
 *
 * Copyright (c) 2016-2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.boteco.test;

import org.mockito.verification.VerificationMode;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.mockito.Mockito.verify;

/**
 * A class that holds useful predicates for testing purposes.
 */
public class Predicates {

  public static Predicate<MessageProcessor> accept(IncomeMessage message) {
    return messageProcessor -> messageProcessor.canProcess(message);
  }

  public static Predicate<MessageProcessor> notAccept(IncomeMessage message) {
    return accept(message).negate();
  }

  public static <T> Predicate<T> receive(Consumer<T> verification) {
    return t -> {
      verification.accept(verify(t));
      return true;
    };
  }

  public static <T> Predicate<T> receive(Consumer<T> verification, VerificationMode mode) {
    return t -> {
      verification.accept(verify(t, mode));
      return true;
    };
  }

}
