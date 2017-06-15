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

package tools.devnull.boteco.plugins.manager;

import tools.devnull.boteco.AlwaysActive;
import tools.devnull.boteco.message.IncomeMessage;
import tools.devnull.boteco.message.MessageProcessor;
import tools.devnull.boteco.message.checker.Command;
import tools.devnull.boteco.plugins.manager.spi.PluginManager;

import java.util.List;

/**
 * A message processor that can manage plugins for a specific channel and target.
 */
@AlwaysActive
@Command("plugin")
public class PluginManagerMessageProcessor implements MessageProcessor {

  private final PluginManager activator;

  public PluginManagerMessageProcessor(PluginManager activator) {
    this.activator = activator;
  }

  @Override
  public void process(IncomeMessage message) {
    message.command()
        .on("enabled", String.class, (name) -> {
          boolean active = activator.isEnabled(name, message.location());
          if (active) {
            message.reply("[p]Yes, the plugin is enabled for this channel[/p]");
          } else {
            message.reply("[n]No, the plugin is disabled for this channel[/n]");
          }
        })
        .on("disable", List.class, (plugins) -> {
          plugins.forEach(name -> activator.disable(name.toString(), message.location()));
          message.reply("Plugin(s) disabled successfully!");
        })
        .on("enable", List.class, (plugins) -> {
          plugins.forEach(name -> activator.enable(name.toString(), message.location()));
          message.reply("Plugin(s) enabled successfully!");
        }).execute();
  }

}
