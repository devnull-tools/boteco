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

package tools.devnull.boteco.message;

import tools.devnull.boteco.Name;
import tools.devnull.boteco.message.checker.MessageProcessorChecker;

/**
 * Interface that defines a plugin that can process a message.
 */
public interface MessageProcessor {

  /**
   * Returns the name of this message processor. The name doesn't need to
   * be unique and can be used to any operations such as logging or applying
   * rules.
   * <p>
   * The default implementation checks for a {@link Name} annotation, if it's
   * not present, the default value {@code message-processor} is returned.
   *
   * @return the name of this message processor
   */
  default String name() {
    if (this.getClass().isAnnotationPresent(Name.class)) {
      return this.getClass().getAnnotation(Name.class).value();
    }
    return "message-processor";
  }

  /**
   * Checks if this processor can process the given message.
   * <p>
   * The default implementation uses the {@link MessageProcessorChecker} to
   * process annotations
   *
   * @param message the message to process
   * @return <code>true</code> if this processor can process the given message.
   */
  default boolean canProcess(IncomeMessage message) {
    return new MessageProcessorChecker().canProcess(this, message);
  }

  /**
   * Process the given message.
   *
   * @param message the message to process.
   */
  void process(IncomeMessage message);

}
