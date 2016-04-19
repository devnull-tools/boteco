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

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.domain.ContentFormatter;
import tools.devnull.boteco.domain.IncomeMessage;
import tools.devnull.boteco.domain.MessageProcessor;
import tools.devnull.boteco.domain.ServiceLocator;
import tools.devnull.boteco.domain.client.rest.RestClient;

import java.net.URI;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KBaseMessageProcessor implements MessageProcessor, ServiceLocator {

  private static final Logger logger = LoggerFactory.getLogger(KBaseMessageProcessor.class);

  private final Pattern pattern =
      Pattern.compile("http(s)?://access\\.redhat\\.com/(?<type>solutions|articles)/(?<number>\\d+)");

  @Override
  public boolean canProcess(IncomeMessage message) {
    return pattern.matcher(message.content()).find();
  }

  @Override
  public void process(IncomeMessage message) {
    Matcher matcher = pattern.matcher(message.content());
    while (matcher.find()) {
      String number = matcher.group("number");
      fetch(message, number);
    }
  }

  private void fetch(IncomeMessage message, String number) {
    try {
      URI uri = new URIBuilder()
          .setScheme("https")
          .setHost("api.access.redhat.com")
          .setPath("/rs/search")
          .addParameter("enableElevation", "true")
          .addParameter("fq", "language:(en)")
          .addParameter("q", number)
          .build();

      KBaseSearchResult result = locate(RestClient.class).get(uri)
          .withHeader("Accept", "application/vnd.redhat.solr+json")
          .to(KBaseSearchResult.class);

      ContentFormatter formatter = message.channel().formatter();
      KBaseSearchResult.Doc document = result.results().stream()
          .filter(rightSolution(number))
          .findFirst()
          .orElse(null);

      if (document != null) {
        message.reply("%s %s (%s links)",
            formatter.tag(document.getDocumentKind()),
            formatter.link(document.getTitle(), document.getViewUri()),
            formatter.value(document.getCaseCount())
        );
      }
    } catch (Exception e) {
      logger.error("Error while fetching kbase article", e);
    }
  }

  private Predicate<? super KBaseSearchResult.Doc> rightSolution(String number) {
    return doc -> {
      String solutionId = doc.getSolutionId();
      String documentId = doc.getId();
      // sometimes documents have different solution id and id
      if (solutionId != null) {
        return documentId.equals(number) && solutionId.equals(number);
      } else {
        return documentId.equals(number);
      }
    };
  }

}
