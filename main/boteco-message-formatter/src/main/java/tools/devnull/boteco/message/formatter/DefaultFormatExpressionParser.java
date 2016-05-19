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

package tools.devnull.boteco.message.formatter;

import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.message.FormatExpressionParser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultFormatExpressionParser implements FormatExpressionParser {

  @Override
  public String parse(ContentFormatter formatter, String expression) {
    Map<String, Function<String, String>> map = new HashMap<>();

    map.put("a", formatter::accent);
    map.put("aa", formatter::alternativeAccent);
    map.put("v", formatter::value);
    map.put("p", formatter::positive);
    map.put("n", formatter::negative);
    map.put("t", formatter::tag);
    map.put("e", formatter::error);
    map.put("m", formatter::mention);
    map.put("l", formatter::link);

    StringBuilder result = new StringBuilder(expression);
    map.entrySet().forEach(entry -> replaceText(result, entry.getKey(), entry.getValue()));
    return result.toString();
  }

  private void replaceText(StringBuilder result, String tag, Function<String, String> function) {
    String open = "[" + tag + "]";
    String close = "[/" + tag + "]";
    int begin = result.indexOf(open);
    int end = result.indexOf(close, begin);
    while (begin > -1 && end > -1) {
      String replace = result.substring(begin + open.length(), end);
      String formatted = function.apply(replace);
      result.replace(begin, end + close.length(), formatted);
      begin = result.indexOf(open);
      end = result.indexOf(close, begin);
    }
  }

}
