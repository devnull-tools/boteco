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

import tools.devnull.boteco.BotException;
import tools.devnull.boteco.Param;
import tools.devnull.boteco.Parameters;
import tools.devnull.trugger.util.factory.Context;
import tools.devnull.trugger.util.factory.ContextFactory;
import tools.devnull.trugger.util.factory.CreateException;

import java.lang.reflect.Parameter;
import java.util.function.Function;
import java.util.function.Predicate;

import static tools.devnull.trugger.reflection.ParameterPredicates.annotatedWith;
import static tools.devnull.trugger.reflection.ParameterPredicates.named;
import static tools.devnull.trugger.reflection.ParameterPredicates.type;

public class MessageCommandConverter<E> implements Function<String, E> {

  private final IncomeMessage message;
  private final Class<E> type;

  public MessageCommandConverter(IncomeMessage message, Class<E> type) {
    this.message = message;
    this.type = type;
  }

  @Override
  public E apply(String content) {
    Parameters parameters = type.getAnnotation(Parameters.class);
    String[] values = content.split("\\s+");
    for (String path : parameters.value()) {
      String[] names = path.split("\\s+");
      if (names.length == values.length) {
        ContextFactory factory = new ContextFactory();
        Context context = factory.context();
        context.use(this.message).when(type(IncomeMessage.class));
        for (int i = 0; i < names.length; i++) {
          context.use(values[i]).when(named(names[i]));
          context.use(values[i]).when(
              annotatedWith(Param.class).and(parameterNamed(names[i]))
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

  private Predicate<Parameter> parameterNamed(String name) {
    return p -> p.getAnnotation(Param.class).value().equals(name);
  }

}
