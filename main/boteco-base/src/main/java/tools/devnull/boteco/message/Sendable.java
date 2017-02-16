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
 * Interface that defines something that can be sent through boteco
 * as a message.
 */
public interface Sendable extends Serializable {

  /**
   * Returns the message that describes this object.
   *
   * @return the message that should be sent to the subscribers.
   * @see tools.devnull.boteco.ContentFormatter
   * @see tools.devnull.boteco.message.FormatExpressionParser
   */
  String message();

  /**
   * Returns the title of the message (if applicable).
   *
   * @return the title of the message
   */
  String title();

  /**
   * Returns a url that points to the subject of the message (if applicable).
   *
   * @return a url that points to the subject of the message.
   */
  String url();

  /**
   * Returns the priority of this message.
   *
   * @return the priority of this message.
   */
  default Priority priority() {
    return Priority.NORMAL;
  }

  /**
   * Creates a new sendable object using the given content as the message.
   *
   * @param content the content of the message
   * @return a new sendable object
   */
  static Sendable message(String content) {
    return new SendableObject(content, null, null, Priority.NORMAL);
  }

}
