package tools.devnull.boteco.channel.pushover;

import tools.devnull.boteco.domain.Channel;
import tools.devnull.boteco.domain.ContentFormatter;
import tools.devnull.boteco.domain.MessageSender;
import tools.devnull.boteco.domain.NullContentFormatter;
import tools.devnull.boteco.domain.TargetSelector;
import tools.devnull.boteco.domain.ServiceLocator;

public class PushoverChannel implements Channel, ServiceLocator {
  @Override
  public String name() {
    return "Pushover";
  }

  @Override
  public String id() {
    return "pushover";
  }

  @Override
  public ContentFormatter formatter() {
    return new NullContentFormatter();
  }

  @Override
  public TargetSelector send(String content) {
    return target -> locate(MessageSender.class).send(content).to(target).throught(id());
  }
}
