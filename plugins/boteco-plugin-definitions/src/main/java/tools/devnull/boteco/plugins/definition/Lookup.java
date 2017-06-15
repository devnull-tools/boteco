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
import tools.devnull.boteco.plugins.definition.spi.DefinitionProvider;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static tools.devnull.boteco.Predicates.id;

public class Lookup {

  private final ServiceRegistry registry;
  private final String provider;
  private final String term;

  public Lookup(ServiceRegistry registry, String provider, String term) {
    this.registry = registry;
    this.provider = provider;
    this.term = term;
  }

  public Lookup(ServiceRegistry registry, String term) {
    this.registry = registry;
    this.provider = null;
    this.term = term;
  }

  public String term() {
    return term;
  }

  public List<Definition> lookup() {
    List<Definition> result;
    if (provider == null) {
      result = registry.locate(DefinitionProvider.class).all()
          .stream()
          .map(p -> p.lookup(term))
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    } else {
      DefinitionProvider definitionProvider = registry.locate(DefinitionProvider.class)
          .filter(id(provider))
          .one();
      if (definitionProvider == null) {
        throw new DomainException("Provider " + provider + " not found");
      }
      Definition lookup = definitionProvider.lookup(term);
      if (lookup != null) {
        result = Collections.singletonList(lookup);
      } else {
        result = Collections.emptyList();
      }
    }
    return result;
  }

}
