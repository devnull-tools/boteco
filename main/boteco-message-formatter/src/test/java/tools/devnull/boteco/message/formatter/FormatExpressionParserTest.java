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
import tools.devnull.kodo.Spec;

import java.util.function.Function;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tools.devnull.kodo.Expectation.to;

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
    Spec.given(parser)
        .expect(expression("[a]value[/a]"), to().be("accent: value"))
        .expect(expression("lorem ipsum [a]value[/a] dolor [/a]"),
            to().be("lorem ipsum accent: value dolor [/a]"))

        .expect(expression("[aa]value[/aa]"), to().be("alternative_accent: value"))

        .expect(expression("[v]value[/v]"), to().be("value: value"))
        .expect(expression("[p]value[/p]"), to().be("positive: value"))
        .expect(expression("[n]value[/n]"), to().be("negative: value"))
        .expect(expression("[t]value[/t]"), to().be("tag: value"))
        .expect(expression("[e]value[/e]"), to().be("error: value"))
        .expect(expression("[l]title|url[/l]"), to().be("link: title - url"))

        .expect(expression("[t]value[/a]"), to().be("[t]value[/a]"));

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
