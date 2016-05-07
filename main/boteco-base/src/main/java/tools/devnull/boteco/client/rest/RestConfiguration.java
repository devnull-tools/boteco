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

package tools.devnull.boteco.client.rest;

import java.util.function.Function;

/**
 * Interface that defines a rest configuration.
 *
 * @see RestClient
 */
public interface RestConfiguration {

  /**
   * Adds the given header to invocation
   *
   * @param name  the name of the header
   * @param value the value of the header
   * @return an instance of this object
   */
  RestConfiguration withHeader(String name, Object value);

  /**
   * Uses the given function to extract the body content before returning or parsing it.
   *
   * @param function the function to apply to the returned body content
   * @return an instance of this object
   */
  RestConfiguration extract(Function<String, String> function);

  /**
   * Invokes the rest url and parses the response into an object of the given class.
   *
   * @param type the type of the result
   * @return the parsed result
   */
  <E> E to(Class<? extends E> type);

  /**
   * Invokes the rest url and returns the raw body without parsing
   *
   * @return the raw body returned by the rest url
   */
  String rawBody();

}
