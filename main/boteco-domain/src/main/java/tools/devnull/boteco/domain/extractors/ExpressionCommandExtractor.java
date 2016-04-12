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

package tools.devnull.boteco.domain.extractors;

import tools.devnull.boteco.domain.Command;
import tools.devnull.boteco.domain.CommandExtractor;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * A command extractor that uses an expression to check if the message is a command.
 */
public class ExpressionCommandExtractor implements CommandExtractor, Serializable {

  private static final long serialVersionUID = 3153909150938096646L;
  private final Pattern pattern;

  public ExpressionCommandExtractor(String expression) {
    pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
  }

  @Override
  public boolean isCommand(String content) {
    return pattern.matcher(content).find();
  }

  public Command extract(String content) {
    StringBuilder command = new StringBuilder(
        pattern.matcher(content).replaceFirst("").trim()
    );
    int firstSpace = command.indexOf(" ");
    if (firstSpace < 0) { // no arguments
      return new ExtractedCommand(command.toString(), "");
    } else {
      return new ExtractedCommand(
          command.substring(0, firstSpace),
          command.substring(firstSpace, command.length()).trim()
      );
    }
  }

}
