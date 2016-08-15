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

public class BotecoIrcEventListener implements IRCEventListener {

  private static final Logger logger = LoggerFactory.getLogger(BotecoIrcEventListener.class);

  private final IRCConnection connection;
  private final IrcConfiguration configuration;
  private final IrcChannelsRepository repository;

  public BotecoIrcEventListener(IRCConnection connection,
                                IrcConfiguration configuration,
                                IrcChannelsRepository repository) {
    this.connection = connection;
    this.configuration = configuration;
    this.repository = repository;
  }

  private void joinChannel(String channel) {
    logger.info("Joining channel " + channel);
    this.connection.doJoin(channel);
  }

  @Override
  public void onRegistered() {
    this.repository.channels().forEach(this::joinChannel);
  }

  @Override
  public void onDisconnected() {
    this.connection.doQuit();
    this.connection.close();
    // TODO use a different approach to reconnect to irc in case of failures
    BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
    try {
      Bundle bundle = bundleContext.getBundle();
      bundle.stop();
      bundle.start();
    } catch (Exception e) {
      logger.error(e.getMessage());
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
    logger.info("Received an invite to join " + chan);
    joinChannel(chan);
    this.repository.add(chan);
  }

  @Override
  public void onJoin(String chan, IRCUser user) {

  }

  @Override
  public void onKick(String chan, IRCUser user, String passiveNick, String msg) {
    if (passiveNick.equals(configuration.getNickname())) {
      logger.info(user.getNick() + " kicked me from " + chan);
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
