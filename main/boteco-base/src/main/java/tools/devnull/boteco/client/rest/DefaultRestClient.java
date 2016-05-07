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

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class DefaultRestClient implements RestClient {

  private final HttpClient client;

  public DefaultRestClient(HttpClient client) {
    this.client = client;
  }

  public DefaultRestClient() {
    this(HttpClients.createDefault());
  }

  @Override
  public RestConfiguration post(URI uri) throws IOException {
    return execute(new HttpPost(uri));
  }

  @Override
  public RestConfiguration post(String url) throws IOException {
    return execute(new HttpPost(url));
  }

  @Override
  public RestConfiguration get(URI uri) throws IOException {
    return execute(new HttpGet(uri));
  }

  @Override
  public RestConfiguration get(String url) throws IOException {
    return execute(new HttpGet(url));
  }

  @Override
  public RestConfiguration delete(URI uri) throws IOException {
    return execute(new HttpDelete(uri));
  }

  @Override
  public RestConfiguration delete(String url) throws IOException {
    return execute(new HttpDelete(url));
  }

  @Override
  public RestConfiguration put(URI uri) throws IOException {
    return execute(new HttpPut(uri));
  }

  @Override
  public RestConfiguration put(String url) throws IOException {
    return execute(new HttpPut(url));
  }

  @Override
  public RestConfiguration head(URI uri) throws IOException {
    return execute(new HttpHead(uri));
  }

  @Override
  public RestConfiguration head(String url) throws IOException {
    return execute(new HttpHead(url));
  }

  @Override
  public RestConfiguration options(URI uri) throws IOException {
    return execute(new HttpOptions(uri));
  }

  @Override
  public RestConfiguration options(String url) throws IOException {
    return execute(new HttpOptions(url));
  }

  private RestConfiguration execute(HttpUriRequest request) throws IOException {
    HttpResponse response = client.execute(request);
    String content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
    Gson gson = new Gson();
    return new RestConfiguration() {
      @Override
      public RestConfiguration withHeader(String name, Object value) {
        request.addHeader(name, String.valueOf(value));
        return this;
      }

      @Override
      public String rawBody() {
        return content;
      }

      @Override
      public <E> E to(Class<? extends E> type) {
        return gson.fromJson(content, type);
      }
    };
  }
}
