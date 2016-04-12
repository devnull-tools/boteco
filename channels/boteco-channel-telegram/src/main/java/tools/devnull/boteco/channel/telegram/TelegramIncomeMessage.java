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

package tools.devnull.boteco.channel.telegram;

import org.apache.activemq.ActiveMQConnectionFactory;
import tools.devnull.boteco.domain.Command;
import tools.devnull.boteco.domain.CommandExtractor;
import tools.devnull.boteco.domain.IncomeMessage;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

public class TelegramIncomeMessage implements IncomeMessage {

  private static final long serialVersionUID = -7037612529067018573L;

  private final CommandExtractor extractor;
  private final TelegramPooling.Message message;

  public TelegramIncomeMessage(CommandExtractor extractor, TelegramPooling.Message message) {
    this.extractor = extractor;
    this.message = message;
  }

  @Override
  public String channel() {
    return "telegram";
  }

  @Override
  public String content() {
    String text = message.getText();
    return text == null ? "" : text;
  }

  @Override
  public boolean isPrivate() {
    return message.getChat().getId() > 0;
  }

  @Override
  public boolean isGroup() {
    return message.getChat().getId() < 0;
  }

  @Override
  public String sender() {
    return message.getFrom().getUsername();
  }

  @Override
  public String target() {
    return message.getChat().getId().toString();
  }

  @Override
  public boolean hasCommand() {
    return extractor.isCommand(content());
  }

  @Override
  public Command command() {
    return extractor.extract(content());
  }

  @Override
  public void reply(String content) {
    replyMessage(message.getChat().getId(), content);
  }

  @Override
  public void replySender(String content) {
    replyMessage(message.getFrom().getId(), content);
  }

  private void replyMessage(Integer id, String content) {
    try {
      ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin", "vm://localhost");
      Connection connection = connectionFactory.createConnection();
      connection.start();

      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      Destination destination = session.createQueue("boteco.message.telegram");

      MessageProducer producer = session.createProducer(destination);
      producer.setDeliveryMode(DeliveryMode.PERSISTENT);

      ObjectMessage message = session.createObjectMessage(new TelegramOutcomeMessage(id, content));
      producer.send(message);

      session.close();
      connection.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
