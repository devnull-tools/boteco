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

package tools.devnull.boteco.message;

import tools.devnull.boteco.Channel;
import tools.devnull.boteco.MessageDestination;
import tools.devnull.boteco.user.User;
import tools.devnull.trugger.reflection.Reflection;
import tools.devnull.trugger.util.factory.Context;
import tools.devnull.trugger.util.factory.CreateException;
import tools.devnull.trugger.util.factory.DefaultContext;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tools.devnull.trugger.reflection.ParameterPredicates.type;

/**
 * A function responsible for converting an input string into an object that represents
 * the parameters.
 *
 * @param <E> the type of the parameter object
 */
public class MessageCommandConverter<E> implements Function<String, E> {

  private final IncomeMessage message;
  private final Class<E> type;

  public MessageCommandConverter(IncomeMessage message, Class<E> type) {
    this.message = message;
    this.type = type;
  }

  @Override
  public E apply(String content) {
    List<Constructor<?>> constructors = Reflection.reflect().constructors().in(type);
    for (Constructor constructor : constructors) {
      int stringParameters = stringParameters(constructor);
      List<String> values = split(content, stringParameters);
      if (stringParameters == values.size()) {
        Iterator<String> iterator = values.iterator();
        Context context = new DefaultContext();
        context.use(this.message).when(type(IncomeMessage.class))
            .use(this.message.channel()).when(type(Channel.class))
            .use(this.message.sender()).when(type(Sender.class))
            .use(this.message.destination()).when(type(MessageDestination.class))
            .use(parameter -> {
              User user = this.message.user();
              if (user == null) {
                throw new MessageProcessingException("You're not registered.");
              }
              return user;
            }).when(type(User.class))
            .use(parameter ->  iterator.next()).when(type(String.class));
        try {
          Object[] args = Arrays.stream(constructor.getParameters())
              .map(parameter -> context.resolve(parameter))
              .collect(Collectors.toList())
              .toArray();
          return Reflection.invoke(constructor).withArgs(args);
        } catch (CreateException e) {
          throw new MessageProcessingException("Invalid command parameters.");
        }
      }
    }
    throw new MessageProcessingException("Invalid command parameters.");
  }

  private int stringParameters(Constructor constructor) {
    return Arrays.stream(constructor.getParameters())
        .filter(type(String.class))
        .collect(Collectors.counting())
        .intValue();
  }

  private List<String> split(String content, int limit) {
    return content.isEmpty() ? Collections.emptyList() : Arrays.asList(content.split("\\s+", limit));
  }

}
