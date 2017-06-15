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

package tools.devnull.boteco.message.checkers;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.BotException;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.CheckerClass;
import tools.devnull.boteco.message.checker.IncomeMessageChecker;
import tools.devnull.boteco.message.checker.MessageProcessorChecker;
import tools.devnull.kodo.Spec;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static tools.devnull.kodo.Expectation.to;

public class MessageProcessorCheckerTest {

  private MessageProcessorChecker checker;
  private IncomeMessage message;

  @Before
  public void initialize() {
    checker = new MessageProcessorChecker();
    message = mock(IncomeMessage.class);
  }

  @Test
  public void testWithoutAnnotations() {
    MessageProcessorWithoutAnnotations processor = new MessageProcessorWithoutAnnotations();
    Spec.given(checker)
        .expect(listFor(processor), to().be(List::isEmpty))
        .expect(resultOfVerify(processor), to().be(true));
  }

  @Test
  public void testWithAnnotations() {
    MessageProcessorWithAnnotations processor = new MessageProcessorWithAnnotations();
    Spec.given(checker)
        .expect(listFor(processor), to().not().be(List::isEmpty))
        .expect(verify(processor), to().raise(BotException.class));
  }

  private Function<MessageProcessorChecker, List<IncomeMessageChecker>> listFor(MessageProcessor processor) {
    return messageProcessorChecker -> messageProcessorChecker.createAll(processor);
  }

  private Function<MessageProcessorChecker, Boolean> resultOfVerify(MessageProcessor processor) {
    return messageProcessorChecker -> messageProcessorChecker.canProcess(processor, message);
  }

  private Consumer<MessageProcessorChecker> verify(MessageProcessor processor) {
    return messageProcessorChecker -> messageProcessorChecker.canProcess(processor, message);
  }

  static class MessageProcessorWithoutAnnotations implements MessageProcessor {
    @Override
    public void process(IncomeMessage message) {

    }
  }

  @MyCheck
  static class MessageProcessorWithAnnotations implements MessageProcessor {
    @Override
    public void process(IncomeMessage message) {

    }
  }

  @Retention(RetentionPolicy.RUNTIME)
  @CheckerClass(MyChecker.class)
  @interface MyCheck {

  }

  static class MyChecker implements IncomeMessageChecker {

    private final MyCheck annotation;

    public MyChecker(MyCheck annotation) {
      this.annotation = annotation;
    }

    @Override
    public boolean canProcess(IncomeMessage message) {
      if (annotation != null) {
        throw new BotException();
      }
      return true;
    }

  }

}
