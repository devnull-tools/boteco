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

package tools.devnull.boteco.persistence.irc;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tools.devnull.boteco.channel.irc.IrcChannelsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MongoIrcChannelsRepository implements IrcChannelsRepository {

  private final MongoCollection<Document> list;

  public MongoIrcChannelsRepository(MongoDatabase database) {
    this.list = database.getCollection("ircchannels");
  }

  private boolean contains(String id) {
    return list.find(new Document("_id", id)).iterator().hasNext();
  }

  @Override
  public void add(String channelName) {
    if (!contains(channelName)) {
      list.insertOne(new Document("_id", channelName));
    }
  }

  @Override
  public void remove(String channelName) {
    list.deleteOne(new Document("_id", channelName));
  }

  @Override
  public List<String> channels() {
    List<String> result = new ArrayList<>();
    list.find().forEach((Consumer<Document>) document -> result.add(document.get("_id").toString()));
    return result;
  }

}
