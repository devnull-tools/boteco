/*
 * The MIT License
 *
 * Copyright (c) 2016-2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Interface that defines a rest configuration.
 *
 * @see RestClient
 */
public interface RestConfiguration {

  /**
   * Sets the payload's content
   *
   * @param value the value to set
   * @return a component for selecting the content type to be sent
   */
  ContentTypeSelector with(Object value);

  /**
   * Sets the timeout fot this invocation.
   * <p>
   * Calling this method will override the default timeout.
   *
   * @param amount the amount
   * @param unit   the unit
   * @return an instance of this class
   */
  RestConfiguration timeoutIn(int amount, TimeUnit unit);

  /**
   * Sets the waiting interval after retrying this invocation.
   * <p>
   * Calling this method will override the default interval.
   *
   * @param amount the amount
   * @param unit   the unit
   * @return an instance of this class
   */
  RestConfiguration waitAfterRetry(int amount, TimeUnit unit);

  /**
   * Retries the request in case of a connection error.
   *
   * @param times the amount of times to retry
   * @return an instance of this class
   */
  RestConfiguration retryOnConnectionError(int times);

  /**
   * Retries the request in case of a connection error.
   *
   * @return an instance of this class
   */
  default RestConfiguration retryOnConnectionError() {
    return retryOnConnectionError(1);
  }

  /**
   * Executes an action if the response matches the given predicate.
   *
   * @param predicate the predicate to test
   * @param action    the action to execute
   * @return an instance of this class
   */
  RestConfiguration on(Predicate<RestResponse> predicate, Consumer<RestResponse> action);

  /**
   * Executes the given action if the status code equals the given one
   *
   * @param statusCode the status code to match
   * @param action     the action to execute
   * @return an instance of this class
   */
  default RestConfiguration on(int statusCode, Consumer<RestResponse> action) {
    return on(response -> response.status() == statusCode, action);
  }

  /**
   * Executes the given action if the status code is of type Informational [100..199]
   *
   * @param action the action to execute
   * @return an instance of this class
   */
  default RestConfiguration onInformational(Consumer<RestResponse> action) {
    return on(response -> {
      int statusCode = response.status();
      return statusCode >= 100 && statusCode <= 199;
    }, action);
  }

  /**
   * Executes the given action if the status code is of type Success [200..299]
   *
   * @param action the action to execute
   * @return an instance of this class
   */
  default RestConfiguration onSuccess(Consumer<RestResponse> action) {
    return on(response -> {
      int statusCode = response.status();
      return statusCode >= 200 && statusCode <= 299;
    }, action);
  }

  /**
   * Executes the given action if the status code is of type Redirection [300..399]
   *
   * @param action the action to execute
   * @return an instance of this class
   */
  default RestConfiguration onRedirection(Consumer<RestResponse> action) {
    return on(response -> {
      int statusCode = response.status();
      return statusCode >= 300 && statusCode <= 399;
    }, action);
  }

  /**
   * Executes the given action if the status code is of type Client Error [400..499]
   *
   * @param action the action to execute
   * @return an instance of this class
   */
  default RestConfiguration onClientError(Consumer<RestResponse> action) {
    return on(response -> {
      int statusCode = response.status();
      return statusCode >= 400 && statusCode <= 499;
    }, action);
  }

  /**
   * Executes the given action if the status code is of type Server Error [500..599]
   *
   * @param action the action to execute
   * @return an instance of this class
   */
  default RestConfiguration onServerError(Consumer<RestResponse> action) {
    return on(response -> {
      int statusCode = response.status();
      return statusCode >= 500 && statusCode <= 599;
    }, action);
  }

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
   * Sets the credentials to send as a basic authentication
   *
   * @param user     the username
   * @param password the password
   * @return an instance of this object
   */
  RestConfiguration withAuthentication(String user, String password);

  /**
   * Invokes the rest url and parses the response into an object of the given class.
   *
   * @param type the type of the result
   * @return the parsed result
   */
  <E> RestResult<E> to(Class<? extends E> type) throws IOException;

  /**
   * Invokes the rest url and parses the response into an object of the given type.
   *
   * @param type the type of the result
   * @return the parsed result
   */
  <E> RestResult<E> to(Type type) throws IOException;

  /**
   * Invokes the rest url and parses the response into a list of objects of the given type.
   *
   * @param type the type of the objects in the list
   * @return the parsed result
   */
  <E> RestResult<List<E>> toListOf(Class<E> type) throws IOException;

  /**
   * Sets the date format to use when parsing the object.
   *
   * @param pattern the date format to use
   * @return an instance of this object
   */
  RestConfiguration withDateFormat(String pattern);

  /**
   * Sets the date format to use when parsing the object.
   *
   * @param dateFormat the date format to use
   * @return an instance of this object
   */
  RestConfiguration withDateFormat(DateFormat dateFormat);

  /**
   * Invokes the rest url and returns the raw body without parsing
   *
   * @return the raw body returned by the rest url
   */
  String rawBody() throws IOException;

  /**
   * Executes the rest url ignoring the response content.
   */
  void execute() throws IOException;

}
