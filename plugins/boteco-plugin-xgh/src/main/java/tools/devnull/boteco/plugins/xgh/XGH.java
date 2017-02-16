/*
 * The MIT License
 *
 * Copyright (c) 2017 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.boteco.plugins.xgh;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class XGH {

  private final List<Axiom> axioms = new ArrayList<>(30);

  public XGH(String lang) {
    initialize(new Locale(lang));
  }

  public XGH() {
    initialize(Locale.getDefault());
  }

  private void initialize(Locale locale) {
    ResourceBundle bundle = ResourceBundle.getBundle("tools.devnull.boteco.plugins.xgh.axioms", locale);
    int size = bundle.keySet().size() / 2; // title and description for each axiom
    String format = "axiom.%d.%s";
    for (int i = 1; i <= size; i++) { // axioms starts at 1
      axioms.add(new Axiom(
          i,
          bundle.getString(String.format(format, i, "title")),
          bundle.getString(String.format(format, i, "description"))
      ));
    }
  }

  public Axiom axiom(int number) {
    if (number < 1 || number > axioms.size()) {
      throw new IllegalArgumentException(String.format("Axiom %d is not defined", number));
    }
    return axioms.get(number - 1);
  }

  public List<Axiom> axioms() {
    return new ArrayList<>(axioms);
  }

}
