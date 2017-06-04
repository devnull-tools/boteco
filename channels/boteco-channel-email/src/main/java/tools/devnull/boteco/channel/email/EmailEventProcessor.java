package tools.devnull.boteco.channel.email;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import tools.devnull.boteco.event.EventBus;

import javax.mail.internet.MimeUtility;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tools.devnull.boteco.message.MessageBuilder.message;

/**
 * A processor for events arriving through email messages
 */
public class EmailEventProcessor implements Processor {

  private final EventBus eventBus;
  private final Pattern pattern;

  public EmailEventProcessor(EventBus eventBus, String pattern) {
    this.eventBus = eventBus;
    this.pattern = Pattern.compile(pattern);
  }

  @Override
  public void process(Exchange exchange) throws Exception {
    String subject = MimeUtility.decodeText(exchange.getIn().getHeader("Subject", String.class).trim());
    String content = exchange.getIn().getBody(String.class).trim();
    Matcher matcher = pattern.matcher(subject);
    if (matcher.find()) {
      String id = matcher.group("id");
      String title = matcher.group("title");
      eventBus.broadcast(message(content)
          .withTitle(title))
          .as(id);
    }
  }

}
