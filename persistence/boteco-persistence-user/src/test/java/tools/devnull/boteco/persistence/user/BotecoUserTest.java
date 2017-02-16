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

package tools.devnull.boteco.persistence.user;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.Destination;
import tools.devnull.boteco.InvalidDestinationException;
import tools.devnull.boteco.MessageDestination;
import tools.devnull.boteco.user.User;
import tools.devnull.kodo.Spec;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static tools.devnull.kodo.Expectation.because;
import static tools.devnull.kodo.Expectation.to;

public class BotecoUserTest {

  private User user;
  private MessageDestination primaryDestination;
  private MessageDestination secondaryDestination;

  @Before
  public void initialize() {
    primaryDestination = Destination.channel("mychannel").to("myuser");
    secondaryDestination = Destination.channel("secondary_channel").to("myuser");
    user = new BotecoUser("user", primaryDestination);
    user.addDestination(secondaryDestination);
  }

  @Test
  public void testConstructor() {
    Spec.given(user)
        .expect(User::id, to().be("user"))
        .expect(User::primaryDestination, to().be(primaryDestination))
        .expect(User::destinations, to().have(twoElements()))
        .expect(user -> user.destination("mychannel"), to().be(primaryDestination))
        .expect((Consumer<User>) user -> user.destination("foo"), to().raise(InvalidDestinationException.class));
  }

  @Test
  public void testRemoveDestination() {
    Spec.given(user)
        .expect(removing(primaryDestination), to().raise(InvalidDestinationException.class))

        .when(removing(secondaryDestination))
        .expect(User::destinations, to().have(oneElement()));
  }

  @Test
  public void testChangeDefaultDestination() {
    Spec.given(user)
        .when(primaryDestinationIsSetTo(secondaryDestination))
        .expect(User::primaryDestination, to().be(secondaryDestination))

        .expect(removing(primaryDestination), to().succeed())

        .expect(User::destinations, to().have(oneElement()))

        .expect(removing(secondaryDestination), to().raise(InvalidDestinationException.class))

        .when(primaryDestinationIsSetTo(primaryDestination))

        .expect(User::destinations, to().have(twoElements()),
            because("The destination should be created if a MessageDestination is passed"))

        .expect(User::primaryDestination, to().be(primaryDestination));

    Spec.given(user)
        .when(primaryDestinationIsSetTo("secondary_channel"))
        .expect(User::primaryDestination, to().be(secondaryDestination))

        .expect(removing("mychannel"), to().succeed())

        .expect(User::destinations, to().have(oneElement()))

        .expect(removing("secondary_channel"), to().raise(InvalidDestinationException.class))

        .expect(settingPrimaryDestinationTo("mychannel"), to().raise(InvalidDestinationException.class));
  }

  private Consumer<User> primaryDestinationIsSetTo(MessageDestination destination) {
    return u -> u.setPrimaryDestination(destination);
  }

  private Consumer<User> settingPrimaryDestinationTo(String channel) {
    return u -> u.setPrimaryDestination(channel);
  }

  private Consumer<User> primaryDestinationIsSetTo(String channel) {
    return u -> u.setPrimaryDestination(channel);
  }

  private Predicate<List> oneElement() {
    return list -> list.size() == 1;
  }

  private Predicate<List> twoElements() {
    return list -> list.size() == 2;
  }

  private Consumer<User> removing(MessageDestination destination) {
    return u -> u.removeDestination(destination);
  }

  private Consumer<User> removing(String channel) {
    return u -> u.removeDestination(channel);
  }
}
