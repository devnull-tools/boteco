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

import org.apache.camel.Handler;
import org.apache.camel.language.XPath;
import tools.devnull.boteco.event.EventBus;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static tools.devnull.boteco.event.NotificationBuilder.notification;

public class StatusRssProcessor {

  private final EventBus bus;
  private final int threshold;

  public StatusRssProcessor(EventBus bus, int threshold) {
    this.bus = bus;
    this.threshold = threshold;
  }

  @Handler
  public void process(@XPath("//title") String title,
                      @XPath("//description") String description,
                      @XPath("//pubDate") String pubDate,
                      @XPath("//link") String link) {
    ZonedDateTime published = ZonedDateTime.parse(pubDate, DateTimeFormatter.RFC_1123_DATE_TIME);
    if (ChronoUnit.MINUTES.between(published,
        ZonedDateTime.now()) <= threshold) {
      bus.broadcast(notification(description.replaceAll("</?\\w+>", "").trim())
          .withTitle(title)
          .withUrl(link)
          .build())
          .as("status.redhat");
    }
  }

}
