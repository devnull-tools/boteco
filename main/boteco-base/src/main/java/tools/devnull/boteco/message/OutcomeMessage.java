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

package tools.devnull.boteco.message;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Class that defines a message that can be delivered through a channel.
 */
public class OutcomeMessage implements Serializable, Sendable {

  private static final long serialVersionUID = -6192434926707152224L;

  private final String content;
  private final String target;
  private final Priority priority;
  private final String title;
  private final String url;
  private final Map<String, Object> metadata;
  private final String replyId;

  /**
   * Creates a new outcome message using the given parameters
   *
   * @param target   the target of the message
   * @param content  the content of the message
   * @param priority the message priority
   * @param metadata the metadata map
   * @param replyId  the id to reply (if applicable)
   */
  public OutcomeMessage(String title,
                        String url,
                        String target,
                        String content,
                        Priority priority,
                        Map<String, Object> metadata,
                        String replyId) {
    this.title = title;
    this.url = url;
    this.content = content;
    this.target = target;
    this.priority = priority;
    this.metadata = metadata;
    this.replyId = replyId;
  }

  public String getContent() {
    return content;
  }

  public String getTarget() {
    return target;
  }

  public Priority getPriority() {
    return priority;
  }

  public boolean isHighPriority() {
    return priority == Priority.HIGH;
  }

  public boolean isLowPriority() {
    return priority == Priority.LOW;
  }

  public boolean hasTitle() {
    return this.title != null && !this.title.isEmpty();
  }

  public boolean hasUrl() {
    return this.url != null && !this.url.isEmpty();
  }

  public String getTitle() {
    return title;
  }

  public String getUrl() {
    return url;
  }

  public String getReplyId() {
    return replyId;
  }

  public void ifTitle(Consumer<String> consumer) {
    if (hasTitle()) {
      consumer.accept(title);
    }
  }

  public void ifUrl(Consumer<String> consumer) {
    if (hasUrl()) {
      consumer.accept(url);
    }
  }

  public void ifReply(Consumer<String> consumer) {
    if (replyId != null) {
      consumer.accept(replyId);
    }
  }

  public void eachMetadata(Consumer<Map.Entry<String, Object>> consumer) {
    this.metadata.entrySet().forEach(consumer);
  }

  @Override
  public String message() {
    return this.content;
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
