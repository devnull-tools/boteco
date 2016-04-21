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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/message")
public class MessageService implements ServiceLocator {

  @POST
  @Consumes("application/json")
  @Path("/{channel}")
  public Response sendMessage(@PathParam("channel") String channelId, Message message) {
    Channel channel = locate(Channel.class, String.format("(id=%s)", channelId));
    // if the channel is present, then the message will be delivered as soon as the channel can process it
    if (channel != null) {
      channel.send(message.getContent()).to(message.getTarget());
      return Response.ok().build();
    } else {
      // otherwise, the message will be delivered when channel bundle starts
      locate(MessageSender.class).send(message.getContent())
          .to(message.getTarget())
          .through(channelId);
      return Response.accepted().build();
    }
  }

  @GET
  @Produces("application/json")
  @Path("/channels")
  public Response getAvailableChannels() {
    List<Channel> channels = locateAll(Channel.class, "(id=*)");
    return Response.ok(
        channels.stream()
            .map(channel -> new AvailableChannel(channel.id(), channel.name()))
            .collect(Collectors.toList())
    ).build();
  }

}
