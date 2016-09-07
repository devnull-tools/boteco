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

import tools.devnull.boteco.event.Notifiable;

/**
 * A class that represents an error while processing a message.
 */
public class MessageProcessingError implements Notifiable {

  private static final long serialVersionUID = -9080804000347525917L;

  private final IncomeMessage incomeMessage;
  private final Throwable cause;

  public MessageProcessingError(IncomeMessage incomeMessage,
                                Throwable cause) {
    this.incomeMessage = incomeMessage;
    this.cause = cause;
  }

  /**
   * @return the income
   */
  public IncomeMessage incomeMessage() {
    return this.incomeMessage;
  }

  public Throwable cause() {
    return this.cause;
  }

  @Override
  public String message() {
    return String.format("[%s@%s] %s",
        incomeMessage.sender(),
        incomeMessage.channel().id(),
        cause.getMessage());
  }

}
