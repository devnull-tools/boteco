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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A default implementation of a command extracted by a {@link CommandExtractor}.
 */
public class ExtractedCommand implements MessageCommand {

  private final IncomeMessage incomeMessage;
  private final String name;
  private final String rawArguments;
  private final Map<Class<?>, Function<String, ?>> functions;

  public ExtractedCommand(IncomeMessage incomeMessage, String name, String rawArguments) {
    this.incomeMessage = incomeMessage;
    this.name = name;
    this.rawArguments = rawArguments;
    this.functions = new HashMap<>();

    this.functions.put(String.class, Function.identity());
    this.functions.put(Integer.class, Integer::parseInt);
    this.functions.put(int.class, Integer::parseInt);
    this.functions.put(Long.class, Long::parseLong);
    this.functions.put(long.class, Long::parseLong);
    this.functions.put(Double.class, Double::parseDouble);
    this.functions.put(double.class, Double::parseDouble);
    this.functions.put(List.class, string -> string.isEmpty() ?
        Collections.emptyList() :
        new ArrayList<>(Arrays.asList(string.split("\\s+")))
    );
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public <E> E as(Class<E> type) {
    Function<String, ?> function = functions.get(type);
    if (function == null) {
      function = new MessageCommandConverter<>(this.incomeMessage, type);
    }
    return (E) function.apply(rawArguments);
  }

}
