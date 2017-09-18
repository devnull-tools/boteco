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

package tools.devnull.boteco.plugins.help;

import tools.devnull.boteco.AlwaysActive;
import tools.devnull.boteco.Name;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageCommand;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;
import tools.devnull.boteco.plugin.Plugin;

import java.util.stream.Collectors;

@Name(HelpPlugin.ID)
@Command("help")
@AlwaysActive
public class HelpMessageProcessor implements MessageProcessor {

  private final ServiceRegistry registry;

  public HelpMessageProcessor(ServiceRegistry registry) {
    this.registry = registry;
  }

  @Override
  public void process(IncomeMessage message) {
    MessageCommand command = message.command();
    this.registry.locate(Plugin.class)
        .all().forEach(plugin -> command.on(plugin.id(), () -> message.reply(buildPluginHelp(plugin))));
    command.orElseReturn(buildPluginList());
  }

  private String buildPluginHelp(Plugin plugin) {
    return String.format("[a]%s[/a]: %s", plugin.id(), plugin.description()) +
        buildPluginCommands(plugin) +
        buildPluginNotifications(plugin) +
        buildPluginProviderTypes(plugin) +
        buildPluginListenTo(plugin);
  }

  private String buildPluginCommands(Plugin plugin) {
    if (plugin.availableCommands().isEmpty()) {
      return "";
    }
    return String.format("%n[aa]Commands:[/aa]%n%s",
        plugin.availableCommands().stream()
            .map(this::buildCommandDescription)
            .collect(Collectors.joining("\n")));
  }

  private String buildCommandDescription(tools.devnull.boteco.plugin.Command command) {
    return command.parameters().isEmpty() ?
        String.format("- [v]%s[/v]: %s", command.name(), command.description()) :
        String.format("- [v]%s[/v] [aa]%s[/aa]: %s",
            command.name(),
            command.parameters().stream().collect(Collectors.joining(" ")),
            command.description()
        );
  }

  private String buildPluginNotifications(Plugin plugin) {
    if (plugin.notifications().isEmpty()) {
      return "";
    }
    return String.format("%n[aa]Notifications:[/aa]%n%s",
        plugin.notifications().stream()
            .map(notification -> String.format("- [v]%s[/v]: %s", notification.name(), notification.description()))
            .collect(Collectors.joining("\n")));
  }

  private String buildPluginProviderTypes(Plugin plugin) {
    if (plugin.providerTypes().isEmpty()) {
      return "";
    }
    return String.format("%n[aa]Provider Types:[/aa]%n%s",
        plugin.providerTypes().stream()
            .map(provider -> String.format("- [v]%s[/v]", provider))
            .collect(Collectors.joining("\n")));
  }

  private String buildPluginListenTo(Plugin plugin) {
    if (plugin.listensTo().isEmpty()) {
      return "";
    } else {
      return "\n[aa]Listens to:[/aa] " + plugin.listensTo();
    }
  }

  private String buildPluginList() {
    return "List of plugins:\n" +
        this.registry.locate(Plugin.class).all().stream()
            .map(plugin -> String.format("- [a]%s[/a]: %s", plugin.id(), plugin.description()))
            .collect(Collectors.joining("\n")) +
        "\nTo get help for a specific plugin, send the command [v]help <plugin>[/v]";
  }

}
