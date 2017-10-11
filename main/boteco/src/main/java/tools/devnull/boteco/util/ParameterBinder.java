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

package tools.devnull.boteco.util;

import tools.devnull.trugger.reflection.Reflection;
import tools.devnull.trugger.util.factory.Context;
import tools.devnull.trugger.util.factory.DefaultContext;

import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
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
  private final String datePattern;

  public ParameterBinder(Class<E> type) {
    this.type = type;
    this.splitter = new ParameterSplitter();
    this.functions = new HashMap<>();
    this.contextConfiguration = (context -> {
    });
    this.datePattern = "yyyy-MM-dd";
    initialize();
  }

  private ParameterBinder(Class<E> type,
                          Function<String, List<String>> splitter,
                          Consumer<Context> contextConfiguration,
                          String datePattern) {
    this.type = type;
    this.splitter = splitter;
    this.contextConfiguration = contextConfiguration;
    this.datePattern = datePattern;
    this.functions = new HashMap<>();
    initialize();
  }

  private void initialize() {
    DateFormat dateFormat = new SimpleDateFormat(datePattern);
    dateFormat.setLenient(false);

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern);

    this.functions.put(String.class, Function.identity());
    this.functions.put(Integer.class, Integer::parseInt);
    this.functions.put(int.class, Integer::parseInt);
    this.functions.put(Long.class, Long::parseLong);
    this.functions.put(long.class, Long::parseLong);
    this.functions.put(Double.class, Double::parseDouble);
    this.functions.put(double.class, Double::parseDouble);
    this.functions.put(List.class, splitter::apply);
    this.functions.put(String[].class, content -> content.split(","));
    this.functions.put(Date.class, content -> {
      try {
        return dateFormat.parse(content);
      } catch (ParseException e) {
        throw new IllegalArgumentException("Invalid date format");
      }
    });
    this.functions.put(LocalDate.class, content -> {
      try {
        return LocalDate.parse(content, dateTimeFormatter);
      } catch (DateTimeParseException e) {
        throw new IllegalArgumentException("Invalid date format");
      }
    });
  }

  public ParameterBinder<E> context(Consumer<Context> contextConfiguration) {
    return new ParameterBinder<>(this.type, this.splitter, contextConfiguration, this.datePattern);
  }

  public ParameterBinder<E> split(Function<String, List<String>> splitter) {
    return new ParameterBinder<>(this.type, splitter, this.contextConfiguration, this.datePattern);
  }

  @Override
  public E apply(String content) {
    if (this.functions.containsKey(type)) {
      return (E) this.functions.get(type).apply(content);
    }
    List<Constructor<?>> constructors = Reflection.reflect().constructors().from(type);
    List<String> values = splitter.apply(content);
    for (Constructor constructor : constructors) {
      int stringParameters = bindableParameters(constructor);
      if (stringParameters == values.size()) {
        Iterator<String> iterator = values.iterator();
        Context context = new DefaultContext();
        contextConfiguration.accept(context);
        context.use(parameter -> this.functions.get(parameter.getType()).apply(iterator.next()))
            .when(parameter -> this.functions.containsKey(parameter.getType()));

        Object[] args = Arrays.stream(constructor.getParameters())
            .map(parameter -> context.resolve(parameter).value())
            .collect(Collectors.toList())
            .toArray();
        return Reflection.invoke(constructor).withArgs(args);
      }
    }
    throw new IllegalArgumentException();
  }

  private int bindableParameters(Constructor constructor) {
    return (int) Arrays.stream(constructor.getParameters())
        .filter(parameter -> this.functions.containsKey(parameter.getType()))
        .count();
  }

}
