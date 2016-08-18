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

package tools.devnull.boteco.plugins.request;

import tools.devnull.boteco.Request;
import tools.devnull.boteco.RequestManager;

/**
 * The default implementation for a {@link RequestManager}.
 * <p>
 * This class uses a repository to persist requests.
 */
public class BotecoRequestManager implements RequestManager {

  private final RequestRepository repository;

  public BotecoRequestManager(RequestRepository repository) {
    this.repository = repository;
  }

  @Override
  public <T> Request<T> request(T target) {
    Request request = new BotecoRequest(target);
    this.repository.save(request);
    return request;
  }

  @Override
  public <T> T find(String token, Class<T> objectType) {
    return this.repository.find(token, objectType);
  }

  @Override
  public <T> T pull(String token, Class<T> objectType) {
    return this.repository.delete(token, objectType);
  }

}
