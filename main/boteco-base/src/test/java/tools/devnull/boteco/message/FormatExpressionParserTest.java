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

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.message.impl.DefaultFormatExpressionParser;
import tools.devnull.kodo.TestScenario;

import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tools.devnull.kodo.Spec.be;
import static tools.devnull.kodo.Spec.should;

public class FormatExpressionParserTest {

  private FormatExpressionParser parser;
  private ContentFormatter formatter;

  @Before
  public void initialize() {
    formatter = mock(ContentFormatter.class);
    parser = new DefaultFormatExpressionParser();

    when(formatter.accent("value")).thenReturn("accent: value");
    when(formatter.alternativeAccent("value")).thenReturn("alternative_accent: value");
    when(formatter.error("value")).thenReturn("error: value");
    when(formatter.link("title", "url")).thenReturn("link: title - url");
    when(formatter.mention("user")).thenReturn("mention: user");
    when(formatter.negative("value")).thenReturn("negative: value");
    when(formatter.positive("value")).thenReturn("positive: value");
    when(formatter.tag("value")).thenReturn("tag: value");
    when(formatter.value("value")).thenReturn("value: value");
  }

  @Test
  public void testExpressionBuild() {
    TestScenario.given(parser)
        .the(expression("[a]value[/a]"), should(be("accent: value")))
        .the(expression("[b]value[/b]"), should(be("accent: value")))
        .the(expression("lorem ipsum [a]value[/a] dolor [/a]"), should(be("lorem ipsum accent: value dolor [/a]")))

        .the(expression("[aa]value[/aa]"), should(be("alternative_accent: value")))
        .the(expression("[i]value[/i]"), should(be("alternative_accent: value")))

        .the(expression("[v]value[/v]"), should(be("value: value")))
        .the(expression("[p]value[/p]"), should(be("positive: value")))
        .the(expression("[n]value[/n]"), should(be("negative: value")))
        .the(expression("[t]value[/t]"), should(be("tag: value")))

        .the(expression("[t]value[/a]"), should(be("[t]value[/a]")));

    verify(formatter, times(3)).accent("value");
    verify(formatter, times(2)).alternativeAccent("value");
    verify(formatter, times(1)).value("value");
    verify(formatter, times(1)).positive("value");
    verify(formatter, times(1)).negative("value");
    verify(formatter, times(1)).tag("value");
  }

  private Function<FormatExpressionParser, ?> expression(String expression) {
    return f -> f.parse(formatter, expression);
  }

}
