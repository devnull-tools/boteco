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

package tools.devnull.boteco.message.formatter;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.ContentFormatter;
import tools.devnull.boteco.message.FormatExpressionParser;
import tools.devnull.kodo.TestScenario;

import java.util.function.Function;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tools.devnull.kodo.Spec.should;

public class FormatExpressionParserTest {

  private FormatExpressionParser parser;
  private ContentFormatter formatter;

  @Before
  public void initialize() {
    formatter = mock(ContentFormatter.class);
    parser = new DefaultFormatExpressionParser();

    when(formatter.normalize(any())).thenAnswer(invocation -> invocation.getArguments()[0]);
    when(formatter.accent("value")).thenReturn("accent: value");
    when(formatter.alternativeAccent("value")).thenReturn("alternative_accent: value");
    when(formatter.error("value")).thenReturn("error: value");
    when(formatter.link("title", "url")).thenReturn("link: title - url");
    when(formatter.negative("value")).thenReturn("negative: value");
    when(formatter.positive("value")).thenReturn("positive: value");
    when(formatter.tag("value")).thenReturn("tag: value");
    when(formatter.value("value")).thenReturn("value: value");
  }

  @Test
  public void testExpressionBuild() {
    TestScenario.given(parser)
        .then(expression("[a]value[/a]"), should().be("accent: value"))
        .then(expression("lorem ipsum [a]value[/a] dolor [/a]"), should().be("lorem ipsum accent: value dolor [/a]"))

        .then(expression("[aa]value[/aa]"), should().be("alternative_accent: value"))

        .then(expression("[v]value[/v]"), should().be("value: value"))
        .then(expression("[p]value[/p]"), should().be("positive: value"))
        .then(expression("[n]value[/n]"), should().be("negative: value"))
        .then(expression("[t]value[/t]"), should().be("tag: value"))
        .then(expression("[e]value[/e]"), should().be("error: value"))
        .then(expression("[l]title|url[/l]"), should().be("link: title - url"))

        .then(expression("[t]value[/a]"), should().be("[t]value[/a]"));

    verify(formatter, times(2)).accent("value");
    verify(formatter, times(1)).alternativeAccent("value");
    verify(formatter, times(1)).value("value");
    verify(formatter, times(1)).positive("value");
    verify(formatter, times(1)).negative("value");
    verify(formatter, times(1)).tag("value");
    verify(formatter, times(1)).error("value");
    verify(formatter, times(1)).link("title", "url");
    verify(formatter, times(10)).normalize(any());
  }

  private Function<FormatExpressionParser, String> expression(String expression) {
    return f -> f.parse(formatter, expression);
  }

}
