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

package tools.devnull.boteco.event;

import tools.devnull.boteco.message.Priority;

/**
 * A class to create notifications
 */
public class NotificationBuilder {

  private final String message;
  private Priority priority = Priority.NORMAL;
  private String title;
  private String url;

  private NotificationBuilder(String message) {
    this.message = message;
  }

  public NotificationBuilder withPriority(Priority priority) {
    this.priority = priority;
    return this;
  }

  public NotificationBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public NotificationBuilder withUrl(String url) {
    this.url = url;
    return this;
  }

  public Notifiable build() {
    return new Notification(message, priority, title, url);
  }

  private static class Notification implements Notifiable {

    private static final long serialVersionUID = -4314808647779314529L;

    private final String message;
    private final Priority priority;
    private final String title;
    private final String url;

    private Notification(String message, Priority priority, String title, String url) {
      this.message = message;
      this.priority = priority;
      this.title = title;
      this.url = url;
    }

    @Override
    public String message() {
      return message;
    }

    @Override
    public Priority priority() {
      return priority;
    }

    @Override
    public String title() {
      return title;
    }

    @Override
    public String url() {
      return url;
    }
  }

  public static NotificationBuilder notification(String message) {
    return new NotificationBuilder(message);
  }

}
