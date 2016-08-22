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
import tools.devnull.kodo.TestScenario;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static tools.devnull.kodo.Spec.should;

public class BotecoUserTest {

  private User user;
  private MessageDestination defaultDestination;
  private MessageDestination secondaryDestination;

  @Before
  public void initialize() {
    defaultDestination = Destination.channel("mychannel").to("myuser");
    secondaryDestination = Destination.channel("secondary_channel").to("myuser");
    user = new BotecoUser("user", defaultDestination);
    user.addDestination(secondaryDestination);
  }

  @Test
  public void testConstructor() {
    TestScenario.given(user)
        .then(User::id, should().be("user"))
        .then(User::primaryDestination, should().be(defaultDestination))
        .then(User::destinations, should().have(twoElements()));
  }

  @Test
  public void testRemoveDestination() {
    TestScenario.given(user)
        .then(removing(defaultDestination), should().raise(InvalidDestinationException.class))

        .when(removing(secondaryDestination))
        .then(User::destinations, should().have(oneElement()));
  }

  @Test
  public void testChangeDefaultDestination() {
    TestScenario.given(user)
        .when(defaultDestinationIsSetTo(secondaryDestination))
        .then(User::primaryDestination, should().be(secondaryDestination))

        .then(removing(defaultDestination), should().succeed())

        .then(User::destinations, should().have(oneElement()))

        .then(removing(secondaryDestination), should().raise(InvalidDestinationException.class))

        .when(defaultDestinationIsSetTo(defaultDestination))

        .then(User::destinations, should().have(twoElements()))
        .then(User::primaryDestination, should().be(defaultDestination));

    TestScenario.given(user)
        .when(defaultDestinationIsSetTo("secondary_channel"))
        .then(User::primaryDestination, should().be(secondaryDestination))

        .then(removing(defaultDestination), should().succeed())

        .then(User::destinations, should().have(oneElement()))

        .then(removing(secondaryDestination), should().raise(InvalidDestinationException.class))

        .when(defaultDestinationIsSetTo("mychannel"))

        .then(User::destinations, should().have(twoElements()))
        .then(User::primaryDestination, should().be(defaultDestination));
  }

  private Consumer<User> defaultDestinationIsSetTo(MessageDestination destination) {
    return u -> u.setPrimaryDestination(destination);
  }

  private Consumer<User> defaultDestinationIsSetTo(String channel) {
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

}
