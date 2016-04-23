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

package tools.devnull.boteco.plugins.karma;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.ServiceLocator;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KarmaMessageProcessor implements MessageProcessor, ServiceLocator {

  private final Pattern pattern =
      Pattern.compile("(?<term>\\S+)(?<operation>\\+\\+|\\-\\-)(\\s|$)");

  private final MongoCollection<Document> karmas;
  private final Gson gson;

  public KarmaMessageProcessor(MongoDatabase database) {
    this.karmas = database.getCollection("karmas");
    this.gson = new Gson();
  }

  @Override
  public String id() {
    return "karma";
  }

  @Override
  public boolean canProcess(IncomeMessage message) {
    return message.isGroup() && pattern.matcher(message.content()).find();
  }

  @Override
  public void process(IncomeMessage message) {
    Matcher matcher = pattern.matcher(message.content());
    while (matcher.find()) {
      String term = matcher.group("term");
      String operation = matcher.group("operation");
      int updatedValue = 0;
      switch (operation) {
        case "++":
          updatedValue = operateKarma(term, Karma::raise);
          break;
        case "--":
          updatedValue = operateKarma(term, Karma::lower);
          break;
      }
      ContentFormatter f = message.channel().formatter();
      String value;
      if (updatedValue > 0) {
        value = f.positive(updatedValue);
      } else if (updatedValue < 0) {
        value = f.negative(updatedValue);
      } else {
        value = f.value(updatedValue);
      }
      message.reply("%s has now %s points of karma", f.accent(term), value);
    }
  }

  private Karma findKarma(String term) {
    BasicDBObject query = new BasicDBObject();
    query.put("_id", term);
    Document result = karmas.find(query).first();
    return result == null ? new Karma(term) : gson.fromJson(result.toJson(), Karma.class);
  }

  private void updateKarma(Karma karma) {
    BasicDBObject query = new BasicDBObject();
    query.put("_id", karma.name());
    // remove karma
    if (karma.value() != 0) {
      karmas.updateOne(query, new Document("$set", new Document().append("value", karma.value())));
    } else {
      karmas.findOneAndDelete(query);
    }

  }

  private int operateKarma(String term, Consumer<Karma> operation) {
    term = term.toLowerCase();
    Karma karma = findKarma(term);
    operation.accept(karma);
    updateKarma(karma);
    return karma.value();
  }

}
