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

package tools.devnull.boteco.plugins.request;

import tools.devnull.boteco.BotException;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.message.MessageSender;
import tools.devnull.boteco.request.Request;
import tools.devnull.boteco.request.RequestListener;
import tools.devnull.boteco.request.RequestManager;
import tools.devnull.boteco.request.Verifiable;

import static tools.devnull.boteco.Predicates.eq;
import static tools.devnull.boteco.Predicates.serviceProperty;

/**
 * The default implementation for a {@link RequestManager}.
 * <p>
 * This class uses a repository to persist requests.
 */
public class BotecoRequestManager implements RequestManager {

  private final RequestRepository repository;
  private final MessageSender messageSender;
  private final ServiceRegistry serviceRegistry;

  public BotecoRequestManager(RequestRepository repository,
                              MessageSender messageSender,
                              ServiceRegistry serviceRegistry) {
    this.repository = repository;
    this.messageSender = messageSender;
    this.serviceRegistry = serviceRegistry;
  }

  @Override
  public String create(Verifiable object, String type, String description) {
    String token = this.repository.create(object, type);
    //TODO externalize this to allow customization of the text
    messageSender.send("To confirm '[aa]" + description + "[/aa]' use the token '[a]" + token + "[/a]'.\n" +
        "If you didn't request this, just ignore this message.")
        .withTitle("Please confirm your request")
        .to(object.tokenDestination());
    return token;
  }

  @Override
  public boolean confirm(String token) {
    Request request = this.repository.pull(token);
    if (request != null) {
      RequestListener listener = serviceRegistry.locate(RequestListener.class)
          .filter(serviceProperty("request", eq(request.type())))
          .one();
      if (listener != null) {
        listener.onConfirm(request);
        return true;
      }
      throw new BotException("No listener found for request of type " + request.type());
    }
    return false;
  }

}
