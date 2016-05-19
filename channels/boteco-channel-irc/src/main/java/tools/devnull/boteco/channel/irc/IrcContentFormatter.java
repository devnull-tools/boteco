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

package tools.devnull.boteco.channel.irc;

import tools.devnull.boteco.ContentFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IrcContentFormatter implements ContentFormatter {

  enum Color {
    // IRC colors sorted by code

    WHITE,
    BLACK,
    BLUE,
    GREEN,
    RED,
    BROWN,
    PURPLE,
    ORANGE,
    YELLOW,
    LIGHT_GREEN,
    CYAN,
    LIGHT_CYAN,
    LIGHT_BLUE,
    PINK,
    GREY,
    LIGHT_GREY;

  }

  private String colorize(String content, Color color) {
    return String.format("\u0003%02d%s\u0003", color.ordinal(), content);
  }

  @Override
  public String accent(String content) {
    return colorize(content, Color.YELLOW);
  }

  @Override
  public String alternativeAccent(String content) {
    return colorize(content, Color.ORANGE);
  }

  @Override
  public String positive(String content) {
    return colorize(content, Color.GREEN);
  }

  @Override
  public String negative(String content) {
    return colorize(content, Color.RED);
  }

  @Override
  public String value(String content) {
    return colorize(content, Color.PINK);
  }

  @Override
  public String error(String content) {
    return colorize(content, Color.RED);
  }

  @Override
  public String link(String content) {
    Matcher matcher = Pattern.compile("^(?<title>.+)\\s*:\\s*<(?<url>.+)>$").matcher(content);
    return matcher.find() ? String.format("%s <%s>", matcher.group("title").trim(), matcher.group("url")) : content;
  }

  @Override
  public String tag(String content) {
    return String.format("[%s]", colorize(content, Color.ORANGE));
  }

  @Override
  public String mention(String user) {
    return user;
  }

}
