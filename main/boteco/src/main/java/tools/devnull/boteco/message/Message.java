package tools.devnull.boteco.message;

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.user.User;

import java.io.Serializable;

/**
 * Interface that defines a message sent to the bot.
 */
public interface Message extends Serializable {

  /**
   * Returns the channel this message belongs.
   *
   * @return the channel this message belongs.
   */
  Channel channel();

  /**
   * Returns the content of this message.
   *
   * @return the content of this message.
   */
  String content();

  /**
   * Returns the sender of this message.
   *
   * @return the sender of this message.
   */
  Sender sender();

  /**
   * Returns the recognized user associated with this message
   *
   * @return the user that sent this message
   */
  User user();

  /**
   * Returns the target of this message.
   *
   * @return the target of this message.
   */
  String target();

  /**
   * Returns <code>true</code> if this message was sent in private.
   *
   * @return <code>true</code> if this message was sent in private
   */
  boolean isPrivate();

  /**
   * Returns <code>true</code> if this message was sent to a group.
   *
   * @return <code>true</code> if this message was sent to a group
   */
  boolean isGroup();

  /**
   * Return the id for replying to this message (if applicable).
   *
   * @return the id for replying to this message.
   * @see OutcomeMessageConfiguration#replyingTo(String)
   */
  String replyTo();

}
