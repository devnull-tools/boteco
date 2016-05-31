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

package tools.devnull.boteco.plugins.stocks;

import com.google.gson.annotations.SerializedName;
import tools.devnull.boteco.message.IncomeMessage;

public class StockResult {

  @SerializedName("t")
  private String name;

  @SerializedName("e")
  private String exchange;

  @SerializedName("l_cur")
  private String lastTraded;

  @SerializedName("lt")
  private String date;

  @SerializedName("pcls_fix")
  private String openedValue;

  @SerializedName("c")
  private double variation;

  @SerializedName("cp")
  private double variationPer;

  public void reply(IncomeMessage message) {
    message.sendBack(String.format(
        "[a]%s[/a] as of [aa]%s[/aa]: Opened at [v]%s[/v]. Last traded at [v]%s[/v]. Varied %.2f (%.2f%%)",
        name + ":" + exchange,
        date,
        openedValue,
        lastTraded,
        variation,
        variationPer
    ));
  }

}
