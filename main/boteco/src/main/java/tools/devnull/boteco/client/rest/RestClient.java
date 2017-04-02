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

package tools.devnull.boteco.client.rest;

import java.net.URI;

/**
 * Interface that defines a rest client.
 */
public interface RestClient {

  /**
   * Invokes the url using the POST http verb.
   *
   * @param uri the uri to invoke
   * @return a component to select the type of the result
   */
  RestConfiguration post(URI uri);

  /**
   * Invokes the url using the POST http verb.
   *
   * @param url the url to invoke
   * @return a component to select the type of the result
   */
  RestConfiguration post(String url);

  /**
   * Invokes the url using the GET http verb.
   *
   * @param uri the uri to invoke
   * @return a component to select the type of the result
   */
  RestConfiguration get(URI uri);

  /**
   * Invokes the url using the GET http verb.
   *
   * @param url the url to invoke
   * @return a component to select the type of the result
   */
  RestConfiguration get(String url);

  /**
   * Invokes the url using the DELETE http verb.
   *
   * @param uri the uri to invoke
   * @return a component to select the type of the result
   */
  RestConfiguration delete(URI uri);

  /**
   * Invokes the url using the DELETE http verb.
   *
   * @param url the url to invoke
   * @return a component to select the type of the result
   */
  RestConfiguration delete(String url);

  /**
   * Invokes the url using the PUT http verb.
   *
   * @param uri the uri to invoke
   * @return a component to select the type of the result
   */
  RestConfiguration put(URI uri);

  /**
   * Invokes the url using the PUT http verb.
   *
   * @param url the url to invoke
   * @return a component to select the type of the result
   */
  RestConfiguration put(String url);

  /**
   * Invokes the url using the HEAD http verb.
   *
   * @param uri the uri to invoke
   * @return a component to select the type of the result
   */
  RestConfiguration head(URI uri);

  /**
   * Invokes the url using the HEAD http verb.
   *
   * @param url the url to invoke
   * @return a component to select the type of the result
   */
  RestConfiguration head(String url);

  /**
   * Invokes the url using the OPTIONS http verb.
   *
   * @param uri the uri to invoke
   * @return a component to select the type of the result
   */
  RestConfiguration options(URI uri);

  /**
   * Invokes the url using the OPTIONS http verb.
   *
   * @param url the url to invoke
   * @return a component to select the type of the result
   */
  RestConfiguration options(String url);

}
