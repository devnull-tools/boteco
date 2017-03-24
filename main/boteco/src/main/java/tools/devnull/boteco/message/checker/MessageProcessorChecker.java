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

package tools.devnull.boteco.message.checker;

import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.trugger.util.factory.ComponentFactory;

import java.util.List;

/**
 * A class that verifies if a {@link MessageProcessor} can process an
 * {@link IncomeMessage} based on annotations.
 * <p>
 * A Message Processor can be annotated to indicate that a check needs to be
 * done in order to process a message. The default implementation of
 * {@link MessageProcessor#canProcess(IncomeMessage)} already uses this class
 * to do the check so the only thing you need to do is to annotated your processors.
 * <p>
 * A check annotation is any annotation that is annotated with a {@link CheckerClass}
 * annotation that indicates the class that implements the check.
 * <p>
 * If the checker class needs the values of the annotation then it must declare a
 * constructor that receives the annotation in order to get those values.
 *
 * @see Channel
 * @see Command
 * @see Group
 * @see Private
 */
public class MessageProcessorChecker {

  private final ComponentFactory<CheckerClass, IncomeMessageChecker> factory;

  public MessageProcessorChecker() {
    this.factory = new ComponentFactory<>(CheckerClass.class);
  }

  /**
   * Checks if the given message processor accepts the given income message based on the
   * message processor's annotations.
   *
   * @param messageProcessor the message processor that has check annotations
   * @param message          the message to check against the annotations
   * @return {@code true} if all checks pass (or if there is no checks to do at all)
   */
  public boolean canProcess(MessageProcessor messageProcessor, IncomeMessage message) {
    return createAll(messageProcessor).stream()
        .allMatch(checker -> checker.canProcess(message));
  }

  /**
   * Creates all message checkers associated with the annotations in the given
   * message processor
   *
   * @param messageProcessor the message processor that has check annotations
   * @return the checkers associated with the given message processor's check annotations
   */
  public List<IncomeMessageChecker> createAll(MessageProcessor messageProcessor) {
    return this.factory.createAll(messageProcessor.getClass());
  }

}
