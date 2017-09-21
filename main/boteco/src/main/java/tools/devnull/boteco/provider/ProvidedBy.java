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

package tools.devnull.boteco.provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates the "type" service property of the service that provides the annotated
 * type.
 * <p>
 * Services that defines the annotated type as the service property "type" can be
 * retrieved by the methods {@link tools.devnull.boteco.ServiceRegistry#providerOf(Class)},
 * {@link tools.devnull.boteco.ServiceRegistry#providerOf(Class, String)} and
 * {@link tools.devnull.boteco.ServiceRegistry#providersOf(Class)}.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProvidedBy {

  /**
   * Defines the value of the service property "type" that should be present on
   * providers of the annotated type.
   *
   * @return the value of the service property "type"
   */
  String value();

}
