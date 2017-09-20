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

package tools.devnull.boteco.plugin;

/**
 * A class that represent a listening for some pattern in a message
 * by a Plugin.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public class Listener {

  private final String pattern;
  private final String response;

  public Listener(String pattern, String response) {
    this.pattern = pattern;
    this.response = response;
  }

  /**
   * Returns the pattern that is searched in messages
   *
   * @return the pattern that is searched in messages
   */
  public String pattern() {
    return pattern;
  }

  /**
   * The response given by the plugin when a message matches
   * the pattern
   *
   * @return the response
   */
  public String response() {
    return response;
  }

  public static ListenerBuilder listenTo(String pattern) {
    return response -> new Listener(pattern, response);
  }

  /**
   * Interface for helping create Command objects
   */
  public interface ListenerBuilder {

    /**
     * Defines what is the response for the message filtered
     *
     * @param description the response for the message filtered
     * @return the created listener object
     */
    Listener respondWith(String description);

  }

}
