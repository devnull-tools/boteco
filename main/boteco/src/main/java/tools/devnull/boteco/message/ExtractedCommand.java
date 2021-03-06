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

import tools.devnull.boteco.Action;
import tools.devnull.boteco.Channel;
import tools.devnull.boteco.MessageLocation;
import tools.devnull.boteco.OsgiServiceRegistry;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.util.OsgiParameterResolver;
import tools.devnull.boteco.util.ParameterBinder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static tools.devnull.trugger.reflection.ParameterPredicates.type;

/**
 * A default implementation of a command extracted by a {@link CommandExtractor}.
 */
public class ExtractedCommand implements MessageCommand {

  private final Message message;
  private final String name;
  private final String rawArguments;
  private final Map<String, Action> actions;
  private Consumer<String> defaultAction;

  public ExtractedCommand(Message message, String name, String rawArguments) {
    this.message = message;
    this.name = name;
    this.rawArguments = rawArguments;
    this.actions = new HashMap<>();

    this.defaultAction = string -> {
      throw new MessageProcessingException("Invalid action, possible actions: \n" +
          actions.keySet().stream().collect(Collectors.joining("\n")));
    };
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public <T> T as(Class<T> type) {
    return convert(rawArguments, type);
  }

  @Override
  public List<String> asList() {
    return as(List.class);
  }

  @Override
  public String asString() {
    return as(String.class);
  }

  private <T> T convert(String string, Class<T> type) {
    try {
      return new ParameterBinder<>(type)
          .context(context -> {
            OsgiServiceRegistry registry = new OsgiServiceRegistry();
            OsgiParameterResolver osgiResolver = new OsgiParameterResolver(registry);

            context.use(this.message)
                .when(type(IncomeMessage.class))

                .use(this.message.channel())
                .when(type(Channel.class))

                .use(this.message.sender())
                .when(type(Sender.class))

                .use(this.message.location())
                .when(type(MessageLocation.class))

                .use(registry)
                .when(type(ServiceRegistry.class))

                // try to lookup implementations by default using the osgi registry
                .use(osgiResolver)
                .byDefault();
          }).apply(string);
    } catch (Exception e) {
      throw new MessageProcessingException("Invalid command parameters.");
    }
  }

  @Override
  public <T> MessageCommand on(String actionName, Class<T> parameterType, Consumer<T> consumer) {
    this.actions.put(actionName,
        () -> consumer.accept(convert(resolveParameterString(rawArguments), parameterType)));
    return this;
  }

  @Override
  public MessageCommand on(String actionName, Class<? extends Action> actionClass) {
    this.actions.put(actionName,
        () -> convert(resolveParameterString(rawArguments), actionClass));
    return this;
  }

  private String resolveParameterString(String string) {
    return string.contains(" ") ? string.replaceFirst("\\S+\\s", "").trim() : "";
  }

  @Override
  public MessageCommand on(String actionName, Consumer<String> action) {
    return on(actionName, String.class, action);
  }

  @Override
  public MessageCommand on(String actionName, Action action) {
    this.actions.put(actionName, action);
    return this;
  }

  @Override
  public void orElse(Consumer<String> action) {
    this.defaultAction = action;
    execute();
  }

  @Override
  public void execute() {
    String actionName = rawArguments.split("\\s+")[0];
    if (this.actions.containsKey(actionName)) {
      this.actions.get(actionName).execute();
    } else {
      this.defaultAction.accept(rawArguments);
    }
  }

}
