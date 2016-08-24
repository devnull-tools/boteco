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

import tools.devnull.boteco.Param;
import tools.devnull.trugger.reflection.Reflection;
import tools.devnull.trugger.util.factory.Context;
import tools.devnull.trugger.util.factory.ContextFactory;
import tools.devnull.trugger.util.factory.CreateException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static tools.devnull.trugger.reflection.ParameterPredicates.annotatedWith;
import static tools.devnull.trugger.reflection.ParameterPredicates.named;
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
    String[] values = split(content);
    for (Constructor constructor : constructors) {
      List<String> names = getParameters(constructor);
      if (names.size() == values.length) {
        ContextFactory factory = new ContextFactory();
        Context context = factory.context();
        context.use(this.message).when(type(IncomeMessage.class));
        for (int i = 0; i < names.size(); i++) {
          context.use(values[i]).when(named(names.get(i)));
          context.use(values[i]).when(
              annotatedWith(Param.class).and(parameterNamed(names.get(i)))
          );
        }
        try {
          return factory.create(type);
        } catch (CreateException e) {
          throw new MessageProcessingException("Invalid command parameters.");
        }
      }
    }
    throw new MessageProcessingException("Invalid command parameters.");
  }

  private List<String> getParameters(Constructor constructor) {
    return Arrays.stream(constructor.getParameters())
        .filter(annotatedWith(Param.class))
        .map(parameter -> {
          String value = parameter.getAnnotation(Param.class).value();
          return value.isEmpty() ? parameter.getName() : value;
        }).collect(Collectors.toList());
  }

  private Predicate<Parameter> parameterNamed(String name) {
    return p -> p.getAnnotation(Param.class).value().equals(name);
  }

  private String[] split(String content) {
    return content.isEmpty() ? new String[0] : content.split("\\s+");
  }

}
