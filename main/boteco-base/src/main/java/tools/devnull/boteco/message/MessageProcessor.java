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

import tools.devnull.boteco.message.checker.MessageProcessorChecker;

/**
 * Interface that defines a plugin that can process a message.
 */
public interface MessageProcessor {

  /**
   * Returns this message processor key. The key doesn't need to be unique and
   * can be used to any operations such as logging or applying rules.
   *
   * @return the key of this message processor
   */
  default String key() {
    return null;
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
