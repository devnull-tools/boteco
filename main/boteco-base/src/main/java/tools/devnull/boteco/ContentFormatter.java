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

package tools.devnull.boteco;

/**
 * Interface that defines a formatter for content sent through channels.
 *
 * @see Channel#formatter()
 */
public interface ContentFormatter {

  /**
   * Adds an accent to the content
   *
   * @param content the content to format
   * @return the formatted content
   */
  String accent(Object content);

  /**
   * Adds an alternative accent to the content
   *
   * @param content the content to format
   * @return the formatted content
   */
  String alternativeAccent(Object content);

  /**
   * Adds a positive format to the content.
   * <p>
   * Positive formats can be used to highlight positive numbers
   * or positive messages.
   *
   * @param content the content to format
   * @return the formatted content
   */
  String positive(Object content);

  /**
   * Adds a negative format to the content.
   * <p>
   * Negative formats can be used to highlight negative numbers
   * or negative messages.
   *
   * @param content the content to format
   * @return the formatted content
   */
  String negative(Object content);

  /**
   * Adds a value format to the content.
   * <p>
   * Value formats can be used to highlight numbers.
   *
   * @param content the content to format
   * @return the formatted content
   */
  String value(Object content);

  /**
   * Adds an error format to the content.
   * <p>
   * Error formats can be used to highlight error messages.
   *
   * @param content the content to format
   * @return the formatted content
   */
  String error(Object content);

  /**
   * Adds a link format to the given url
   *
   * @param title the title of the link
   * @param url   the url of the link
   * @return the formatted content
   */
  String link(String title, String url);

  /**
   * Adds a tag format to the given content.
   *
   * @param content the content to format
   * @return the formatted content
   */
  String tag(Object content);

  /**
   * Formats a mention to the given user
   *
   * @param user the user to mention
   * @return the formatted content
   */
  String mention(String user);

  /**
   * Adds a number format to the given value.
   * <p>
   * The default implementation adds a {@link #positive(Object)} format
   * if the number is positive and a {@link #negative(Object)} format if
   * the number is negative.
   *
   * @param value the value to format
   * @return the formatted value
   */
  default String number(int value) {
    return number((double) value, "%.0f");
  }

  /**
   * Adds a number format to the given value.
   * <p>
   * The default implementation adds a {@link #positive(Object)} format
   * if the number is positive and a {@link #negative(Object)} format if
   * the number is negative.
   *
   * @param value the value to format
   * @return the formatted value
   */
  default String number(double value, String format) {
    String content = String.format(format, value);
    if (value > 0) {
      return positive(content);
    } else if (value < 0) {
      return negative(content);
    } else {
      return value(content);
    }
  }

}
