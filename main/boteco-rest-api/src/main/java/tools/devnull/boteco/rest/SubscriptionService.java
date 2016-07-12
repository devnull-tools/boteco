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

package tools.devnull.boteco.rest;

import tools.devnull.boteco.event.SubscriptionManager;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/subscription")
public class SubscriptionService {

  private final SubscriptionManager subscriptionManager;

  public SubscriptionService(SubscriptionManager subscriptionManager) {
    this.subscriptionManager = subscriptionManager;
  }

  @PUT
  @Path("/{token}")
  public Response confirm(@PathParam("token") String token) {
    subscriptionManager.confirm(token);
    return Response.ok().build();
  }

  @POST
  @Path("/{channel}/{target}/{event}")
  public Response subscribe(@PathParam("event") String eventId,
                            @PathParam("target") String subscriberTarget,
                            @PathParam("channel") String subscriberChannel) {
    subscriptionManager.subscribe()
        .target(subscriberTarget)
        .ofChannel(subscriberChannel)
        .withConfirmation()
        .toEvent(eventId);
    return Response.ok().build();
  }

  @DELETE
  @Path("/{channel}/{target}/{event}")
  public Response unsubscribe(@PathParam("event") String eventId,
                              @QueryParam("target") String subscriberTarget,
                              @QueryParam("channel") String subscriberChannel) {
    subscriptionManager.unsubscribe()
        .target(subscriberTarget)
        .ofChannel(subscriberChannel)
        .withConfirmation()
        .fromEvent(eventId);
    return Response.ok().build();
  }

}