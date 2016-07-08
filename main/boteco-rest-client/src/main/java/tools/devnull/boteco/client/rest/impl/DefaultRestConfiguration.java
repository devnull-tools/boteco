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

package tools.devnull.boteco.client.rest.impl;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.client.rest.RestConfiguration;
import tools.devnull.boteco.client.rest.RestResponse;
import tools.devnull.boteco.client.rest.RestResult;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class DefaultRestConfiguration implements RestConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(DefaultRestClient.class);

  private final CloseableHttpClient client;
  private final HttpContext context;
  private final HttpRequestBase request;
  private final GsonBuilder gsonBuilder;
  private final Map<Predicate<RestResponse>, Consumer<RestResponse>> actionsMap;
  private Function<String, String> function = (string) -> string;
  private int timeoutRetries;
  private int retryInterval;

  public DefaultRestConfiguration(CloseableHttpClient client,
                                  HttpContext context,
                                  HttpRequestBase request,
                                  Properties configuration) {
    this.client = client;
    this.context = context;
    this.request = request;
    this.gsonBuilder = new GsonBuilder();
    this.actionsMap = new HashMap<>();
    this.configureTimeout(configuration);
  }

  private void configureTimeout(Properties configuration) {
    this.retryOnTimeout(Integer.parseInt(configuration.getProperty("timeout.retries", "0")));
    this.timeoutIn(Integer.parseInt(configuration.getProperty("timeout.value", "10000")), TimeUnit.MILLISECONDS);
    this.waitAfterRetry(Integer.parseInt(configuration.getProperty("timeout.interval", "500")), TimeUnit.MILLISECONDS);
  }

  @Override
  public RestConfiguration retryOnTimeout(int times) {
    this.timeoutRetries = times;
    return this;
  }

  @Override
  public RestConfiguration waitAfterRetry(int amount, TimeUnit unit) {
    this.retryInterval = (int) unit.toMillis(amount);
    return this;
  }

  @Override
  public RestConfiguration timeoutIn(int amount, TimeUnit unit) {
    int timeout = (int) unit.toMillis(amount);
    this.request.setConfig(RequestConfig.custom()
        .setConnectionRequestTimeout(timeout)
        .setConnectTimeout(timeout)
        .setSocketTimeout(timeout)
        .build());
    return this;
  }

  @Override
  public RestConfiguration on(Predicate<RestResponse> predicate, Consumer<RestResponse> action) {
    this.actionsMap.put(predicate, action);
    return this;
  }

  @Override
  public RestConfiguration withHeader(String name, Object value) {
    request.addHeader(name, String.valueOf(value));
    return this;
  }

  @Override
  public RestConfiguration extract(Function<String, String> function) {
    this.function = function;
    return this;
  }

  @Override
  public RestConfiguration withDateFormat(String pattern) {
    this.gsonBuilder.setDateFormat(pattern);
    return this;
  }

  @Override
  public RestConfiguration withDateFormat(DateFormat dateFormat) {
    this.gsonBuilder.registerTypeAdapter(Date.class, new DateTypeAdapter(dateFormat));
    return this;
  }

  @Override
  public String rawBody() throws IOException {
    RestResponse response = getResponse(timeoutRetries);
    return function.apply(response.content());
  }

  @Override
  public void execute() throws IOException {
    getResponse(timeoutRetries);
  }

  @Override
  public <E> RestResult<E> to(Class<? extends E> type) throws IOException {
    try {
      return new DefaultRestResult<>(gsonBuilder.create().fromJson(rawBody(), type));
    } catch (JsonSyntaxException e) {
      logger.error("Error while parsing JSON", e);
      return new DefaultRestResult<>(null);
    }
  }

  @Override
  public <E> RestResult<E> to(Type type) throws IOException {
    try {
      return new DefaultRestResult<>(gsonBuilder.create().fromJson(rawBody(), type));
    } catch (JsonSyntaxException e) {
      logger.error("Error while parsing JSON", e);
      return new DefaultRestResult<>(null);
    }
  }

  private RestResponse getResponse(int retries) throws IOException {
    try {
      CloseableHttpResponse httpResponse = client.execute(request, context);

      HttpEntity entity = httpResponse.getEntity();
      String content = entity == null ? "" : IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
      httpResponse.close();

      DefaultRestResponse response = new DefaultRestResponse(content,
          httpResponse.getStatusLine().getStatusCode(),
          httpResponse.getStatusLine().getReasonPhrase());
      this.actionsMap.entrySet().stream()
          .filter(entry -> entry.getKey().test(response))
          .findFirst()
          .ifPresent(entry -> entry.getValue().accept(response));
      return response;
    } catch (SocketTimeoutException e) {
      if (retries == 0) {
        logger.info("Request timeout");
        return new DefaultRestResponse(null, HttpStatus.SC_REQUEST_TIMEOUT, null);
      } else {
        logger.info("Request timeout, retrying...");
        if (retryInterval > 0) {
          try {
            Thread.sleep(retryInterval);
          } catch (InterruptedException e1) {
            logger.error("Error while waiting for retry", e);
          }
        }
        return getResponse(retries - 1);
      }
    }
  }

}
