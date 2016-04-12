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

package tools.devnull.boteco.channel.telegram;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * A class that manages the offset number that should be passed to the
 * Telegram Bot API.
 * <p>
 * This class is necessary because without an offset, Telegram will keep
 * delivering the same messages.
 */
public class TelegramOffsetManager {

  private final AtomicInteger nextId;

  /**
   * Creates a new object using 0 as the initial offset.
   */
  public TelegramOffsetManager() {
    this(0);
  }

  /**
   * Creates a new object using the given initial offset.
   *
   * @param initialOffset the initial offset to use
   */
  public TelegramOffsetManager(int initialOffset) {
    this.nextId = new AtomicInteger(initialOffset);
  }

  public int nextId() {
    return this.nextId.intValue();
  }

  public void process(TelegramPooling pooling, Consumer<TelegramPooling> consumer) {
    Integer updateId = pooling.getUpdateId();
    if (updateId >= this.nextId.get()) {
      this.nextId.set(updateId + 1);
      consumer.accept(pooling);
    }
  }

}
