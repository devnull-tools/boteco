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

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tools.devnull.boteco.plugins.irc.spi.IrcIgnoreList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Igore list for IRC implemented as a mongo collection
 */
public class MongoIrcIgnoreList implements IrcIgnoreList {

  private final MongoCollection<Document> list;

  public MongoIrcIgnoreList(MongoDatabase database) {
    this.list = database.getCollection("ircignorelist");
  }

  @Override
  public void add(String nickname) {
    if (!contains(nickname)) {
      this.list.insertOne(new Document("_id", nickname));
    }
  }

  @Override
  public void remove(String nickname) {
    this.list.deleteOne(new Document("_id", nickname));
  }

  @Override
  public List<String> list() {
    List<String> result = new ArrayList<>();
    this.list.find().forEach((Consumer<Document>) document -> result.add(document.get("_id").toString()));
    return result;
  }

  @Override
  public boolean contains(String nickname) {
    return this.list.find(new BasicDBObject("_id", nickname)).iterator().hasNext();
  }

}
