/*
 * The MIT License
 *
 * Copyright (c) 2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.boteco.util;

import tools.devnull.trugger.reflection.Reflection;
import tools.devnull.trugger.util.factory.Context;
import tools.devnull.trugger.util.factory.CreateException;
import tools.devnull.trugger.util.factory.DefaultContext;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParameterBinder<E> implements Function<String, E> {

  private final Class<E> type;
  private final Function<String, List<String>> splitter;
  private final Consumer<Context> contextConfiguration;
  private final Map<Class<?>, Function<String, ?>> functions;

  public ParameterBinder(Class<E> type) {
    this.type = type;
    this.splitter = new ParameterSplitter();
    this.functions = new HashMap<>();
    this.contextConfiguration = (context -> {
    });
    initialize();
  }

  protected ParameterBinder() {
    this.type = Reflection.reflect().genericType().in(this);
    this.splitter = new ParameterSplitter();
    this.functions = new HashMap<>();
    this.contextConfiguration = (context -> {
    });
    initialize();
  }

  private ParameterBinder(Class<E> type,
                          Function<String, List<String>> splitter,
                          Consumer<Context> contextConfiguration) {
    this.type = type;
    this.splitter = splitter;
    this.contextConfiguration = contextConfiguration;
    this.functions = new HashMap<>();
    initialize();
  }

  private void initialize() {
    this.functions.put(String.class, Function.identity());
    this.functions.put(Integer.class, Integer::parseInt);
    this.functions.put(int.class, Integer::parseInt);
    this.functions.put(Long.class, Long::parseLong);
    this.functions.put(long.class, Long::parseLong);
    this.functions.put(Double.class, Double::parseDouble);
    this.functions.put(double.class, Double::parseDouble);
    this.functions.put(List.class, string -> string.isEmpty() ?
        Collections.emptyList() : splitter.apply(string)
    );
  }

  public ParameterBinder<E> context(Consumer<Context> contextConfiguration) {
    return new ParameterBinder<>(this.type, this.splitter, contextConfiguration);
  }

  public ParameterBinder<E> split(Function<String, List<String>> splitter) {
    return new ParameterBinder<>(this.type, splitter, this.contextConfiguration);
  }

  @Override
  public E apply(String content) {
    if (this.functions.containsKey(type)) {
      return (E) this.functions.get(type).apply(content);
    }
    List<Constructor<?>> constructors = Reflection.reflect().constructors().in(type);
    List<String> values = splitter.apply(content);
    for (Constructor constructor : constructors) {
      int stringParameters = bindableParameters(constructor);
      if (stringParameters == values.size()) {
        Iterator<String> iterator = values.iterator();
        Context context = new DefaultContext();
        contextConfiguration.accept(context);
        context.use(parameter -> this.functions.get(parameter.getType()).apply(iterator.next()))
            .when(parameter -> this.functions.containsKey(parameter.getType()));

        try {
          Object[] args = Arrays.stream(constructor.getParameters())
              .map(context::resolve)
              .collect(Collectors.toList())
              .toArray();
          return Reflection.invoke(constructor).withArgs(args);
        } catch (CreateException e) {
          throw new IllegalArgumentException(e.getCause());
        }
      }
    }
    throw new IllegalArgumentException();
  }

  private int bindableParameters(Constructor constructor) {
    return (int) Arrays.stream(constructor.getParameters())
        .filter(
            parameter -> this.functions.containsKey(parameter.getType())
        ).count();
  }

}
