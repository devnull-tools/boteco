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

package tools.devnull.boteco.plugins.redhat;

import org.junit.Test;
import tools.devnull.boteco.message.Priority;
import tools.devnull.kodo.Spec;

import static tools.devnull.kodo.Expectation.to;

public class RssDescriptionParserTest {

  private String multiEntryDescription = "<p><small>Mar 13, 10:36 EDT</small><br><strong>Resolved</strong> - " +
      "The SSO issue has been resolved.</p><p><small>Mar 13, 10:28 EDT</small><br><strong>Investigating</strong>" +
      " - We are currently experiencing an issue with our Single Sign-on (SSO) authentication service, resulting in" +
      " degraded application performance and some users being unable to log in to the Customer Portal. We are working" +
      " to resolve the problem as quickly as possible.</p>";

  private String singleEntryDescription = "<p><small>Mar 13, 10:28 EDT</small><br><strong>Investigating</strong>" +
      " - We are currently experiencing an issue with our Single Sign-on (SSO) authentication service, resulting in" +
      " degraded application performance and some users being unable to log in to the Customer Portal. We are working" +
      " to resolve the problem as quickly as possible.</p>";

  private String title = "SSO Major Incident";

  private String url = "http://status.redhat.com";

  private RssDescriptionParser parser = new RssDescriptionParser();

  @Test
  public void testSingleEntry() {
    Spec.given(statusFor(singleEntryDescription))
        .expect(Status::priority, to().be(Priority.HIGH))
        .expect(Status::title, to().be("[t]Investigating[/t] [a]" + title + "[/a]"))
        .expect(Status::url, to().be(url))
        .expect(Status::message, to().be("[aa]Mar 13, 10:28 EDT[/aa]: We are currently experiencing an issue with our Single Sign-on (SSO) authentication service, resulting in degraded application performance and some users being unable to log in to the Customer Portal. We are working to resolve the problem as quickly as possible."));
  }

  @Test
  public void testMultiEntry() {
    Spec.given(statusFor(multiEntryDescription))
        .expect(Status::priority, to().be(Priority.NORMAL))
        .expect(Status::title, to().be("[t]Resolved[/t] [a]" + title + "[/a]"))
        .expect(Status::url, to().be(url))
        .expect(Status::message, to().be("[aa]Mar 13, 10:36 EDT[/aa]: The SSO issue has been resolved."));
  }

  private Status statusFor(String description) {
    return parser.parse(description, title, url);
  }

}
