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

/**
 * Interface that defines a component for building
 * outcome messages to be sent.
 */
public interface OutcomeMessageConfiguration extends DestinationConfiguration {

  /**
   * Adds a header to this message.
   * <p>
   * Headers can be used to customize the message in different channels.
   *
   * @param headerName  the header name
   * @param headerValue the header value
   * @return a reference to this object
   */
  OutcomeMessageConfiguration with(String headerName, Object headerValue);

  /**
   * Sets the priority.
   * <p>
   * Note that not all channels support priorities.
   *
   * @param priority the priority to set
   * @return a reference to this object
   */
  OutcomeMessageConfiguration withPriority(Priority priority);

  /**
   * Sets the title
   *
   * @param title the title to set
   * @return a reference to this object
   */
  OutcomeMessageConfiguration withTitle(String title);

  /**
   * Sets the url
   *
   * @param url the url to set
   * @return a reference to this object
   */
  OutcomeMessageConfiguration withUrl(String url);

  @Override
  OutcomeMessageConfiguration to(String target);

}
