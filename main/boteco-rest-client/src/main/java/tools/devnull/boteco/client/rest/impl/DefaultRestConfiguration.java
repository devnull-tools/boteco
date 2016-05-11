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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import tools.devnull.boteco.client.rest.RestConfiguration;
import tools.devnull.boteco.client.rest.RestResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class DefaultRestConfiguration implements RestConfiguration {

  private final HttpClient client;
  private final HttpUriRequest request;
  private Function<String, String> function = (string) -> string;

  public DefaultRestConfiguration(HttpClient client, HttpUriRequest request) {
    this.client = client;
    this.request = request;
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
  public String rawBody() throws IOException {
    HttpResponse response = client.execute(request);
    String content = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
    return function.apply(content);
  }

  @Override
  public <E> RestResult<E> to(Class<? extends E> type) throws IOException {
    try {
      Gson gson = new Gson();
      return new DefaultRestResult<>(gson.fromJson(rawBody(), type));
    } catch (JsonSyntaxException e) {
      return new DefaultRestResult<>(null);
    }
  }

}
