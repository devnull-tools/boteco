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

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.client.rest.RestClient;
import tools.devnull.boteco.client.rest.RestConfiguration;

import java.net.URI;
import java.util.Properties;

public class DefaultRestClient implements RestClient {

  private static final Logger logger = LoggerFactory.getLogger(DefaultRestClient.class);

  private final CloseableHttpClient client;
  private final HttpClientContext context;

  public DefaultRestClient(Properties configuration) {
    logger.info("Configuring client");
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    cm.setMaxTotal(Integer.parseInt(configuration.getProperty("client.max.total", "200")));
    cm.setDefaultMaxPerRoute(Integer.parseInt(configuration.getProperty("client.max.perRoute", "20")));

    // Create AuthCache instance
    AuthCache authCache = new BasicAuthCache();
    // Generate BASIC scheme object and add it to the local auth cache
    BasicScheme basicAuth = new BasicScheme();

    CredentialsProvider provider = new BasicCredentialsProvider();

    configuration.entrySet().stream()
        .filter(entry -> entry.getKey().toString().startsWith("auth.") && entry.getKey().toString().endsWith(".host"))
        .forEach(entry -> {
          String host = entry.getValue().toString();
          String id = entry.getKey().toString().replaceAll("^auth\\.(\\S+)\\.host$", "$1");
          logger.info("Adding authentication for " + id);
          int port = Integer.parseInt(configuration.getProperty("auth." + id + ".port", "-1"));
          String scheme = configuration.getProperty("auth." + id + ".scheme", null);
          authCache.put(new HttpHost(host, port, scheme), basicAuth);
          provider.setCredentials(
              new AuthScope(host, AuthScope.ANY_PORT),
              new UsernamePasswordCredentials(configuration.getProperty("auth." + id + ".credentials")));
        });

    // Add AuthCache to the execution context
    this.context = HttpClientContext.create();
    this.context.setCredentialsProvider(provider);
    this.context.setAuthCache(authCache);

    this.client = HttpClients.custom()
        .setConnectionManager(cm)
        .build();
  }

  @Override
  public RestConfiguration post(URI uri) {
    return execute(new HttpPost(uri));
  }

  @Override
  public RestConfiguration post(String url) {
    return execute(new HttpPost(url));
  }

  @Override
  public RestConfiguration get(URI uri) {
    return execute(new HttpGet(uri));
  }

  @Override
  public RestConfiguration get(String url) {
    return execute(new HttpGet(url));
  }

  @Override
  public RestConfiguration delete(URI uri) {
    return execute(new HttpDelete(uri));
  }

  @Override
  public RestConfiguration delete(String url) {
    return execute(new HttpDelete(url));
  }

  @Override
  public RestConfiguration put(URI uri) {
    return execute(new HttpPut(uri));
  }

  @Override
  public RestConfiguration put(String url) {
    return execute(new HttpPut(url));
  }

  @Override
  public RestConfiguration head(URI uri) {
    return execute(new HttpHead(uri));
  }

  @Override
  public RestConfiguration head(String url) {
    return execute(new HttpHead(url));
  }

  @Override
  public RestConfiguration options(URI uri) {
    return execute(new HttpOptions(uri));
  }

  @Override
  public RestConfiguration options(String url) {
    return execute(new HttpOptions(url));
  }

  private RestConfiguration execute(HttpUriRequest request) {
    return new DefaultRestConfiguration(client, context, request);
  }

}
