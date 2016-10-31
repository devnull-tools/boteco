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
import tools.devnull.boteco.ServiceLocator;
import tools.devnull.boteco.message.MessageSender;
import tools.devnull.boteco.message.OutcomeMessageBuilder;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/channels")
public class MessageService {

  private final MessageSender messageSender;
  private final ServiceLocator serviceLocator;

  public MessageService(MessageSender messageSender, ServiceLocator serviceLocator) {
    this.messageSender = messageSender;
    this.serviceLocator = serviceLocator;
  }

  @POST
  @Consumes("application/json")
  @Path("/{channel}/{target}")
  public Response sendMessage(@PathParam("channel") String channelId,
                              @PathParam("target") String target,
                              Message message) {
    Channel channel = serviceLocator.locate(Channel.class, String.format("(id=%s)", channelId));
    OutcomeMessageBuilder builder = messageSender.send(message.getContent());
    message.getMetadata().entrySet().forEach(entry -> builder.with(entry.getKey(), entry.getValue()));
    builder.to(target)
        .through(channelId);
    // if the channel is present, then the message will be delivered as soon as the channel can process it
    // otherwise, the message will be delivered when the channel bundle starts
    return channel != null ? Response.ok().build() : Response.accepted().build();
  }

  @GET
  @Produces("application/json")
  @Path("/")
  public Response getAvailableChannels() {
    List<Channel> channels = serviceLocator.locateAll(Channel.class, "(id=*)");
    return Response.ok(channels.stream()
        .map(AvailableChannel::new)
        .collect(Collectors.toList()))
        .build();
  }

}
