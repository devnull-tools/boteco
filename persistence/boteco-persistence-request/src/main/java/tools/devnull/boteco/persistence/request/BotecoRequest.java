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

package tools.devnull.boteco.persistence.request;

import com.google.gson.Gson;
import org.bson.Document;
import tools.devnull.boteco.request.Request;

import java.util.Date;

public class BotecoRequest<T> implements Request<T> {

  private final String token;
  private final Date createdAt;
  private final String type;
  private final Gson gson;
  private final Document object;

  public BotecoRequest(Document document) {
    this.token = document.getString("_id");
    this.createdAt = document.getDate("createdAt");
    this.type = document.getString("type");
    this.gson = new Gson();
    this.object = document.get("object", Document.class);
  }

  @Override
  public String token() {
    return this.token;
  }

  @Override
  public String type() {
    return this.type;
  }

  @Override
  public T object(Class<T> type) {
    return this.gson.fromJson(object.toJson(), type);
  }

  @Override
  public Date createdAt() {
    return this.createdAt;
  }

}
