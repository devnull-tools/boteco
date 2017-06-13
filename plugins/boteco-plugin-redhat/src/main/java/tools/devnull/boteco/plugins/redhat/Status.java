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

package tools.devnull.boteco.plugins.redhat;

import tools.devnull.boteco.message.Priority;
import tools.devnull.boteco.Sendable;

/**
 * A class that represents a status from status.redhat.com
 */
public class Status implements Sendable {

  private final String title;
  private final String description;
  private final String url;
  private final Priority priority;

  public Status(String title, String date, String description, String status, String url) {
    this.title = String.format("[t]%s[/t] [a]%s[/a]", status, title);
    this.description = String.format("[aa]%s[/aa]: %s", date, description);
    this.priority = "investigating".equalsIgnoreCase(status) ? Priority.HIGH : Priority.NORMAL;
    this.url = url;
  }

  @Override
  public String message() {
    return description;
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public String url() {
    return url;
  }

  @Override
  public Priority priority() {
    return priority;
  }

}
