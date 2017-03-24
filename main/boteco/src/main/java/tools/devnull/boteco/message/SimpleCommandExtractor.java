/*
 * The MIT License
 *
 * Copyright (c) 2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

import java.io.Serializable;

/**
 * A command extractor that treats all messages as commands
 */
public class SimpleCommandExtractor implements CommandExtractor, Serializable {

  private static final long serialVersionUID = -3269566201774993002L;

  @Override
  public boolean isCommand(IncomeMessage message) {
    return true;
  }

  public MessageCommand extract(IncomeMessage message) {
    StringBuilder command = new StringBuilder(message.content());
    int firstSpace = command.indexOf(" ");
    if (firstSpace < 0) { // no arguments
      return new ExtractedCommand(message, command.toString(), "");
    } else {
      return new ExtractedCommand(message, command.substring(0, firstSpace),
          command.substring(firstSpace, command.length()).trim()
      );
    }
  }

}
