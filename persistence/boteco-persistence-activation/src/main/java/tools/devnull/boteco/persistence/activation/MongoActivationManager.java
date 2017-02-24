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

package tools.devnull.boteco.persistence.activation;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tools.devnull.boteco.MessageLocation;
import tools.devnull.boteco.plugins.activation.spi.MessageProcessorActivationManager;

/**
 * An activation manager that uses MongoDB to store data.
 */
public class MongoActivationManager implements MessageProcessorActivationManager {

  private final MongoCollection<Document> blacklist;

  public MongoActivationManager(MongoDatabase database) {
    this.blacklist = database.getCollection("processorBlacklist");
  }

  @Override
  public void deactivate(String name, MessageLocation location) {
    this.blacklist.insertOne(createDocument(name, location));
  }

  @Override
  public boolean isActive(String name, MessageLocation location) {
    return this.blacklist.find(createDocument(name, location)).first() == null;
  }

  @Override
  public void activate(String name, MessageLocation location) {
    this.blacklist.deleteOne(createDocument(name, location));
  }

  private Document createDocument(String name, MessageLocation location) {
    return new Document("processor", name)
        .append("channel", location.channel())
        .append("target", location.target());
  }

}
