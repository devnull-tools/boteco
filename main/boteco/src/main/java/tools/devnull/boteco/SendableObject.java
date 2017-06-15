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

import tools.devnull.boteco.Sendable;
import tools.devnull.boteco.message.Priority;

/**
 * A default implementation of the Sendable interface
 */
public class SendableObject implements Sendable {
  private static final long serialVersionUID = 4794270950648791486L;

  private final String message;
  private final String title;
  private final String url;
  private final Priority priority;

  public SendableObject(String message, String title, String url) {
    this(message, title, url, Priority.NORMAL);
  }

  public SendableObject(String message, String title, String url, Priority priority) {
    this.message = message;
    this.title = title;
    this.url = url;
    this.priority = priority;
  }

  @Override
  public String message() {
    return this.message;
  }

  @Override
  public String title() {
    return this.title;
  }

  @Override
  public String url() {
    return this.url;
  }

  @Override
  public Priority priority() {
    return this.priority;
  }

}
