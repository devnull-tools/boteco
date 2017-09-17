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

package tools.devnull.boteco.plugins.definition;

import tools.devnull.boteco.plugin.Command;
import tools.devnull.boteco.plugin.Notification;
import tools.devnull.boteco.plugin.Plugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static tools.devnull.boteco.plugin.Command.command;

public class LookupPlugin implements Plugin {

  public static final String ID = "lookup";

  @Override
  public String id() {
    return ID;
  }

  @Override
  public String description() {
    return "Lookups definitions";
  }

  @Override
  public List<Command> availableCommands() {
    return Arrays.asList(
        command("lookup")
            .with("term")
            .does("Lookups the term using all available providers"),
        command("lookup")
            .with("provider", "term")
            .does("Lookups the term using the given provider")
    );
  }

  @Override
  public boolean listenToMessages() {
    return false;
  }

  @Override
  public boolean sendsNotifications() {
    return false;
  }

  @Override
  public List<Notification> notifications() {
    return Collections.emptyList();
  }

}
