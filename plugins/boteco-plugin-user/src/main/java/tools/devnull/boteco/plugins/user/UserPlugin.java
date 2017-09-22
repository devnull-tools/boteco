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

package tools.devnull.boteco.plugins.user;

import tools.devnull.boteco.plugin.Command;
import tools.devnull.boteco.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

import static tools.devnull.boteco.plugin.Command.command;

public class UserPlugin implements Plugin {

  @Override
  public String id() {
    return "user";
  }

  @Override
  public String description() {
    return "Manages users";
  }

  @Override
  public List<Command> availableCommands() {
    return Arrays.asList(
        command("user new")
            .with("id")
            .does("Creates a new user with the given id"),

        command("user link")
            .with("channel", "target")
            .does("Links your user with a new endpoint"),

        command("user link")
            .does("Links your user with the current endpoint"),

        command("user unlink")
            .with("channel")
            .does("Removes the given channel from your user"),

        command("user unlink")
            .does("Removes the current channel from your user"),

        command("user default")
            .with("channel")
            .does("Sets the given channel to receive notifications")
    );
  }

}
