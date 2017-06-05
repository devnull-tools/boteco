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

import tools.devnull.boteco.Sendable;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Class that defines a message that can be delivered through a channel.
 */
public class OutcomeMessage implements Sendable {

  private static final long serialVersionUID = -6192434926707152224L;

  private final Sendable object;
  private final String target;
  private final Map<String, Object> metadata;
  private final String replyId;

  /**
   * Creates a new outcome message using the given parameters
   *
   * @param object   the object that represents the message to send
   * @param target   the target of the message
   * @param metadata the metadata map
   * @param replyId  the id to reply (if applicable)
   */
  public OutcomeMessage(Sendable object,
                        String target,
                        Map<String, Object> metadata,
                        String replyId) {
    this.object = object;
    this.target = target;
    this.metadata = metadata;
    this.replyId = replyId;
  }

  public String target() {
    return target;
  }

  public boolean isHighPriority() {
    return priority() == Priority.HIGH;
  }

  public boolean isLowPriority() {
    return priority() == Priority.LOW;
  }

  public boolean hasTitle() {
    return this.title() != null && !this.title().isEmpty();
  }

  public boolean hasUrl() {
    return this.url() != null && !this.url().isEmpty();
  }

  public String replyId() {
    return replyId;
  }

  public void ifTitle(Consumer<String> consumer) {
    if (hasTitle()) {
      consumer.accept(title());
    }
  }

  public void ifUrl(Consumer<String> consumer) {
    if (hasUrl()) {
      consumer.accept(url());
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
    return object.message();
  }

  @Override
  public String title() {
    return object.title();
  }

  @Override
  public String url() {
    return object.url();
  }

  @Override
  public Priority priority() {
    return object.priority();
  }

}
