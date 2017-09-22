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

import tools.devnull.boteco.DomainException;
import tools.devnull.boteco.ServiceRegistry;
import tools.devnull.boteco.plugins.definition.spi.Definition;
import tools.devnull.boteco.plugins.definition.spi.DefinitionLookup;
import tools.devnull.trugger.Optional;

public class LookupCommand {

  private final ServiceRegistry registry;
  private final String provider;
  private final String term;

  public LookupCommand(ServiceRegistry registry, String provider, String term) {
    this.registry = registry;
    this.provider = provider;
    this.term = term;
  }

  public LookupCommand(ServiceRegistry registry, String term) {
    this.registry = registry;
    this.provider = null;
    this.term = term;
  }

  public String term() {
    return term;
  }

  public Optional<Definition> lookup() {
    if (provider == null) {
      return registry.providerOf(DefinitionLookup.class)
          .orElseThrow(() -> new DomainException("No providers for definitions"))
          .get()
          .lookup(term);
    } else {
      return registry.providerOf(DefinitionLookup.class, provider)
          .orElseThrow(() -> new DomainException("Provider " + provider + " not found"))
          .get()
          .lookup(term);
    }
  }

}
