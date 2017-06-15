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
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.BotException;
import tools.devnull.boteco.client.rest.ContentTypeSelector;
import tools.devnull.boteco.client.rest.RestConfiguration;
import tools.devnull.boteco.client.rest.RestResponse;
import tools.devnull.trugger.Optional;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static tools.devnull.trugger.element.Elements.elements;

public class DefaultRestConfiguration implements RestConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(DefaultRestClient.class);

  private final CloseableHttpClient client;
  private final HttpClientContext context;
  private final HttpRequestBase request;
  private final GsonBuilder gsonBuilder;
  private final Map<Predicate<RestResponse>, Consumer<RestResponse>> actionsMap;
  private Function<String, String> function = (string) -> string;
  private int errorRetries;
  private int retryInterval;

  public DefaultRestConfiguration(CloseableHttpClient client,
                                  HttpClientContext context,
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
    this.retryOnConnectionError(Integer.parseInt(configuration.getProperty("error.retries", "0")));
    this.timeoutIn(Integer.parseInt(configuration.getProperty("timeout.value", "10000")), TimeUnit.MILLISECONDS);
    this.waitAfterRetry(Integer.parseInt(configuration.getProperty("retry.interval", "500")), TimeUnit.MILLISECONDS);
  }

  @Override
  public RestConfiguration retryOnConnectionError(int times) {
    this.errorRetries = times;
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
  public RestConfiguration withAuthentication(String user, String password) {
    // Create AuthCache instance
    AuthCache authCache = new BasicAuthCache();
    // Generate BASIC scheme object and add it to the local auth cache
    BasicScheme basicAuth = new BasicScheme();
    CredentialsProvider provider = new BasicCredentialsProvider();

    URI uri = request.getURI();
    authCache.put(new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()), basicAuth);
    provider.setCredentials(new AuthScope(uri.getHost(), AuthScope.ANY_PORT),
        new UsernamePasswordCredentials(user, password));

    this.context.setCredentialsProvider(provider);
    this.context.setAuthCache(authCache);

    return this;
  }

  public ContentTypeSelector with(Object object) {
    return new ContentTypeSelector() {
      @Override
      public RestConfiguration asJson() {
        return as(gsonBuilder.create().toJson(object), ContentType.APPLICATION_JSON);
      }

      @Override
      public RestConfiguration asPlainText() {
        return as(object.toString(), ContentType.TEXT_PLAIN);
      }

      @Override
      public RestConfiguration asFormUrlEncoded() {
        List<BasicNameValuePair> pairs;
        if (object instanceof Map) {
          pairs = ((Set<Map.Entry>) ((Map) object).entrySet()).stream()
              .map(entry -> new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()))
              .collect(Collectors.toList());
        } else {
          pairs = elements().in(object).stream()
              .map(el -> new BasicNameValuePair(el.name(), el.value().toString()))
              .collect(Collectors.toList());
        }
        return setEntity(new UrlEncodedFormEntity(pairs, Charset.defaultCharset()));
      }

      private RestConfiguration as(String content, ContentType contentType) {
        return setEntity(new StringEntity(content, contentType));
      }

      private RestConfiguration setEntity(HttpEntity entity) {
        if (DefaultRestConfiguration.this.request instanceof HttpEntityEnclosingRequest) {
          ((HttpEntityEnclosingRequest) DefaultRestConfiguration.this.request)
              .setEntity(entity);
        } else {
          throw new BotException("This request doesn't allow entities");
        }
        return DefaultRestConfiguration.this;
      }

    };
  }

  @Override
  public String rawBody() throws IOException {
    RestResponse response = getResponse(errorRetries);
    return function.apply(response.content());
  }

  @Override
  public void execute() throws IOException {
    getResponse(errorRetries);
  }

  @Override
  public <E> Optional<E> to(Class<? extends E> type) throws IOException {
    try {
      return Optional.of(gsonBuilder.create().fromJson(rawBody(), type));
    } catch (JsonSyntaxException e) {
      logger.error("Error while parsing JSON", e);
      return Optional.empty();
    }
  }

  @Override
  public <E> Optional<E> to(Type type) throws IOException {
    try {
      return Optional.of(gsonBuilder.create().fromJson(rawBody(), type));
    } catch (JsonSyntaxException e) {
      logger.error("Error while parsing JSON", e);
      return Optional.empty();
    }
  }

  @Override
  public <E> Optional<List<E>> toListOf(Class<E> type) throws IOException {
    try {
      E[] result = (E[]) gsonBuilder.create().fromJson(rawBody(), Array.newInstance(type, 0).getClass());
      return Optional.of(result != null ? Arrays.asList(result) : Collections.emptyList());
    } catch (JsonSyntaxException e) {
      logger.error("Error while parsing JSON", e);
      return Optional.empty();
    }
  }

  private RestResponse getResponse(int retries) throws IOException {
    try (CloseableHttpResponse httpResponse = client.execute(request, context)) {
      HttpEntity entity = httpResponse.getEntity();
      String content = entity == null ? "" : IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);

      DefaultRestResponse response = new DefaultRestResponse(content,
          httpResponse.getStatusLine().getStatusCode(),
          httpResponse.getStatusLine().getReasonPhrase());
      this.actionsMap.entrySet().stream()
          .filter(entry -> entry.getKey().test(response))
          .findFirst()
          .ifPresent(entry -> entry.getValue().accept(response));
      return response;
    } catch (SocketTimeoutException | UnknownHostException e) {
      logger.error("Request error", e);
      if (retries == 0) {
        return new DefaultRestResponse(null, HttpStatus.SC_REQUEST_TIMEOUT, null);
      } else {
        logger.error("Retrying request");
        if (retryInterval > 0) {
          try {
            Thread.sleep(retryInterval);
          } catch (InterruptedException e1) {
            logger.error("Error while waiting to retry", e1);
          }
        }
        return getResponse(retries - 1);
      }
    } finally {
      this.client.close();
    }
  }

}
