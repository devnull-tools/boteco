package tools.devnull.boteco.message;

import tools.devnull.boteco.Builder;

/**
 * A class used to configure a message.
 */
public class MessageBuilder implements Builder<Sendable> {

  private final String content;
  private Priority priority = Priority.NORMAL;
  private String title;
  private String url;

  private MessageBuilder(String content) {
    this.content = content;
  }

  public MessageBuilder withPriority(Priority priority) {
    this.priority = priority;
    return this;
  }

  public MessageBuilder withTitle(String title) {
    this.title = title;
    return this;
  }

  public MessageBuilder withUrl(String url) {
    this.url = url;
    return this;
  }

  @Override
  public Sendable build() {
    return new SendableObject(content, title, url, priority);
  }

  public static MessageBuilder message(String content) {
    return new MessageBuilder(content);
  }

}
