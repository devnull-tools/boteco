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
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageCommand;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;
import tools.devnull.boteco.plugin.Plugin;

import java.util.List;
import java.util.stream.Collectors;

@Command("help")
@AlwaysActive
public class HelpMessageProcessor implements MessageProcessor {

  private final List<Plugin> plugins;

  public HelpMessageProcessor(List<Plugin> plugins) {
    this.plugins = plugins;
  }

  @Override
  public void process(IncomeMessage message) {
    MessageCommand command = message.command();
    this.plugins.forEach(plugin -> command.on(plugin.id(), () -> message.reply(buildPluginHelp(plugin))));
    command.orElseReturn(buildPluginList());
  }

  private String buildPluginHelp(Plugin plugin) {
    return String.format("[a]%s[/a]: %s%nCommands:%n%s%nNotifications:%n%s",
        plugin.id(),
        plugin.description(),
        buildPluginCommands(plugin),
        buildPluginNotifications(plugin));
  }

  private String buildPluginNotifications(Plugin plugin) {
    if (plugin.notifications().isEmpty()) {
      return  "[a]NONE[/a]";
    }
    return plugin.notifications().stream()
        .map(notification -> String.format("- [v]%s[/v]: %s", notification.name(), notification.description()))
        .collect(Collectors.joining("\n"));
  }

  private String buildPluginCommands(Plugin plugin) {
    if (plugin.notifications().isEmpty()) {
      return  "[a]NONE[/a]";
    }
    return plugin.availableCommands().stream()
        .map(command -> String.format("- [v]%s[/v]: %s", command.name(), command.description()))
        .collect(Collectors.joining("\n"));
  }

  private String buildPluginList() {
    return "List of plugins:\n" +
        this.plugins.stream()
            .map(plugin -> String.format("- [a]%s[/a]: %s", plugin.id(), plugin.description()))
            .collect(Collectors.joining("\n")) +
        "\nTo get help for a specific plugin, send the command [v]help <plugin>[/v]";
  }

}
