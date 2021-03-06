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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the default expression parser for boteco. It uses the pattern {@code [tag]content[/tag]}
 * to denote a format.
 * <p>
 * The following tags are supported:
 * <ul>
 * <li>a: applies {@link ContentFormatter#accent(String)} </li>
 * <li>aa: applies {@link ContentFormatter#alternativeAccent(String)} </li>
 * <li>v: applies {@link ContentFormatter#value(String)} </li>
 * <li>p: applies {@link ContentFormatter#positive(String)} </li>
 * <li>n: applies {@link ContentFormatter#negative(String)} </li>
 * <li>t: applies {@link ContentFormatter#tag(String)} </li>
 * <li>e: applies {@link ContentFormatter#error(String)} </li>
 * <li>l: applies {@link ContentFormatter#link(String, String)} </li>
 * </ul>
 */
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
    map.put("l", content -> formatLink(formatter, content));

    StringBuilder result = new StringBuilder(formatter.normalize(expression));
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

  private String formatLink(ContentFormatter formatter, String content) {
    Matcher matcher = Pattern.compile("^(?<title>[^|]+)\\s*\\|\\s*(?<url>.+)$").matcher(content);
    return matcher.find() ? formatter.link(matcher.group("title").trim(), matcher.group("url")) : content;
  }

}
