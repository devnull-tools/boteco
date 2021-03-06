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

package tools.devnull.boteco.channel.email;

import tools.devnull.boteco.ContentFormatter;

/**
 * A class that formats content for sending as an email
 */
public class EmailContentFormatter implements ContentFormatter {

  private String enclose(String tag, String content) {
    return String.format("<%s>%s</%s>", tag, content, tag);
  }

  @Override
  public String accent(String content) {
    return enclose("b", content);
  }

  @Override
  public String alternativeAccent(String content) {
    return enclose("i", content);
  }

  @Override
  public String positive(String content) {
    return content;
  }

  @Override
  public String negative(String content) {
    return content;
  }

  @Override
  public String value(String content) {
    return content;
  }

  @Override
  public String error(String content) {
    return content;
  }

  @Override
  public String link(String title, String url) {
    return String.format("<a href=\"%s\">%s</a>", url, title);
  }

  @Override
  public String tag(String content) {
    return content;
  }

}
