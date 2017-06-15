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
import tools.devnull.boteco.message.IncomeMessage;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tools.devnull.boteco.message.MessageChecker.check;

public class MessageCheckerTest {

  private Predicate<IncomeMessage> somethingTrue;
  private IncomeMessage incomeMessage;

  @Before
  public void initialize() {
    somethingTrue = mock(Predicate.class);
    incomeMessage = mock(IncomeMessage.class);
    when(somethingTrue.test(incomeMessage)).thenReturn(true);
  }

  @Test
  public void testAccept() {
    assertTrue(check(incomeMessage).accept(somethingTrue));
    verify(somethingTrue).test(incomeMessage);
  }

  @Test
  public void testReject() {
    assertFalse(check(incomeMessage).reject(somethingTrue));
    verify(somethingTrue).test(incomeMessage);
  }

}
