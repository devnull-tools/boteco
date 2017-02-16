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

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.message.MessageSender;
import tools.devnull.boteco.message.OutcomeMessageConfiguration;
import tools.devnull.boteco.message.Priority;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static tools.devnull.boteco.Predicates.id;

@Path("/channels")
public class ChannelService {

  private final MessageSender messageSender;
  private final ServiceRegistry serviceRegistry;

  public ChannelService(MessageSender messageSender, ServiceRegistry serviceRegistry) {
    this.messageSender = messageSender;
    this.serviceRegistry = serviceRegistry;
  }

  @POST
  @Consumes("application/json")
  @Path("/{channel}/{target}")
  public Response sendMessage(@PathParam("channel") String channelId,
                              @PathParam("target") String target,
                              Message message) {
    Channel channel = serviceRegistry.locate(Channel.class).filter(id(channelId)).one();
    OutcomeMessageConfiguration configuration = messageSender.send(message.getContent());

    message.getMetadata().entrySet().forEach(entry -> configuration.with(entry.getKey(), entry.getValue()));

    configuration.withTitle(message.getTitle());
    configuration.withUrl(message.getUrl());

    try {
      configuration.withPriority(Priority.parse(message.getPriority()));
    } catch (IllegalArgumentException e) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Invalid priority").build();
    }

    configuration.to(target)
        .through(channelId);
    // if the channel is present, then the message will be delivered as soon as the channel can process it
    // otherwise, the message will be delivered when the channel bundle starts
    return channel != null ? Response.ok().build() : Response.accepted().build();
  }

  @GET
  @Produces("application/json")
  @Path("/")
  public Response getAvailableChannels() {
    List<Channel> channels = serviceRegistry.locate(Channel.class).all();
    return Response.ok(channels.stream()
        .map(AvailableChannel::new)
        .collect(Collectors.toList()))
        .build();
  }

  @GET
  @Produces("application/json")
  @Path("/{channel}")
  public Response getAvailableChannel(@PathParam("channel") String channelId) {
    Channel channel = serviceRegistry.locate(Channel.class).filter(id(channelId)).one();
    if (channel != null) {
      return Response.ok(new AvailableChannel(channel))
          .build();
    } else {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

}
