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

package tools.devnull.boteco.channel.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.ContentFormatter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelegramContentFormatter implements ContentFormatter {

  private static final Logger logger = LoggerFactory.getLogger(TelegramContentFormatter.class);

  @Override
  public String accent(String content) {
    return "*" + content + "*";
  }

  @Override
  public String alternativeAccent(String content) {
    return "_" + content + "_";
  }

  @Override
  public String positive(String content) {
    return value(content);
  }

  @Override
  public String negative(String content) {
    return value(content);
  }

  @Override
  public String value(String content) {
    return "`" + content + "`";
  }

  @Override
  public String error(String content) {
    return "*" + content + "*";
  }

  @Override
  public String link(String content) {
    Matcher matcher = Pattern.compile("^(?<title>.+)<(?<url>.+)>$").matcher(content);
    if (matcher.find()) {
      String urlString = matcher.group("url");
      String urlTitle = matcher.group("title").trim();
      try {
        URL url = new URL(urlString);
        // checks if the hostname includes at least a dot and two letters because Telegram will only convert it to a
        // link if it follows that pattern
        if (url.getHost().matches(".+\\.\\w{2,}$")) {
          return String.format("[%s](%s)", urlTitle, urlString);
        } else {
          // the link will not be parsed by Telegram but will be visible
          return String.format("%s <%s>", urlTitle, urlString);
        }
      } catch (MalformedURLException e) {
        logger.error("Error while formatting a link", e);
      }
    }
    return content;
  }

  @Override
  public String tag(String content) {
    return value("[" + content + "]");
  }

}
