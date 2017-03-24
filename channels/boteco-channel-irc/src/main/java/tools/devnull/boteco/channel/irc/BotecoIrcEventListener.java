/*
 * The MIT License
 *
 * Copyright (c) 2016-2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.boteco.channel.irc;

import org.apache.camel.component.irc.IrcConfiguration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventListener;
import org.schwering.irc.lib.IRCModeParser;
import org.schwering.irc.lib.IRCUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.devnull.boteco.event.EventBus;

/**
 * A class that listens to IRC events.
 * <p>
 * This listener is mainly based on /invite and /kick events from IRC.
 * Everytime the bot receives an invite, the channel will be recorded in
 * an {@link IrcChannelsRepository} so the bot can join it later in case
 * of reconnection.
 * <p>
 * Also, whenever the bot is kicked from a channel, the channel will be
 * removed from the repository so the bot will not join it anymore.
 * <p>
 * Broadcasts are also available for both events in case anyone needs
 * to be aware of those events ("irc.invited" and "irc.kicked").
 */
public class BotecoIrcEventListener implements IRCEventListener {

  private static final Logger logger = LoggerFactory.getLogger(BotecoIrcEventListener.class);

  private final IRCConnection connection;
  private final IrcConfiguration configuration;
  private final IrcChannelsRepository repository;
  private final EventBus bus;
  private boolean disconnected;

  /**
   * Creates a new listener using the given parameters
   *
   * @param connection    the irc connection to execute operations
   * @param configuration the configuration to get the bot information
   * @param repository    the channel repository for auto join feature
   * @param bus           the message bus to broadcast events
   */
  public BotecoIrcEventListener(IRCConnection connection,
                                IrcConfiguration configuration,
                                IrcChannelsRepository repository,
                                EventBus bus) {
    this.connection = connection;
    this.configuration = configuration;
    this.repository = repository;
    this.bus = bus;
  }

  public boolean isDisconnected() {
    return disconnected;
  }

  public IrcConfiguration getConfiguration() {
    return configuration;
  }

  private void joinChannel(String channel) {
    logger.info("Joining channel " + channel);
    this.connection.doJoin(channel);
  }

  @Override
  public void onRegistered() {
    this.disconnected = false;
    this.repository.channels().forEach(this::joinChannel);
  }

  @Override
  public void onDisconnected() {
    BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
    Bundle bundle = bundleContext.getBundle();
    if (bundle.getState() == Bundle.ACTIVE) {
      this.disconnected = true;
    }
  }

  @Override
  public void onError(String msg) {

  }

  @Override
  public void onError(int num, String msg) {

  }

  @Override
  public void onInvite(String chan, IRCUser user, String passiveNick) {
    String message = user.getNick() + " invited me to join " + chan;
    logger.info(message);
    joinChannel(chan);
    this.repository.add(chan);
    this.bus.broadcast(message).as("irc.invited");
  }

  @Override
  public void onJoin(String chan, IRCUser user) {

  }

  @Override
  public void onKick(String chan, IRCUser user, String passiveNick, String msg) {
    if (passiveNick.equals(configuration.getNickname())) {
      String message = user.getNick() + " kicked me from " + chan;
      logger.info(message);
      this.bus.broadcast(message).as("irc.kicked");
      this.repository.remove(chan);
    }
  }

  @Override
  public void onMode(String chan, IRCUser user, IRCModeParser modeParser) {

  }

  @Override
  public void onMode(IRCUser user, String passiveNick, String mode) {

  }

  @Override
  public void onNick(IRCUser user, String newNick) {

  }

  @Override
  public void onNotice(String target, IRCUser user, String msg) {

  }

  @Override
  public void onPart(String chan, IRCUser user, String msg) {

  }

  @Override
  public void onPing(String ping) {

  }

  @Override
  public void onPrivmsg(String target, IRCUser user, String msg) {

  }

  @Override
  public void onQuit(IRCUser user, String msg) {

  }

  @Override
  public void onReply(int num, String value, String msg) {

  }

  @Override
  public void onTopic(String chan, IRCUser user, String topic) {

  }

  @Override
  public void unknown(String prefix, String command, String middle, String trailing) {

  }

}
