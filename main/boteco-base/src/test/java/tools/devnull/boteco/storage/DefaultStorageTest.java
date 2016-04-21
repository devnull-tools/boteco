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

package tools.devnull.boteco.storage;

import org.junit.Before;
import org.junit.Test;
import tools.devnull.boteco.storage.impl.DefaultObjectStorage;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultStorageTest {

  private ObjectStorage storage;

  @Before
  public void initialize() {
    storage = new DefaultObjectStorage();
  }

  private Storable newObject(Serializable id) {
    Storable object = mock(Storable.class);
    when(object.id()).thenReturn(id);
    return object;
  }

  @Test
  public void testStoreOperation() {
    Storable object = newObject(10);
    storage.store(Storable.class).into("storage-a").value(object);
    assertSame(object, storage.retrieve(Storable.class).from("storage-a").id(10));
    assertNull(storage.retrieve(Storable.class).from("storage-b").id(10));
  }

  @Test
  public void testRetrieveOperation() {
    storage.store(Storable.class).into("storage-a").value(newObject(10));
    storage.store(Storable.class).into("storage-a").value(newObject(20));
    storage.store(Storable.class).into("storage-a").value(newObject(30));

    assertEquals(3, storage.retrieve(Storable.class).from("storage-a").all().size());

    assertEquals(10, storage.retrieve(Storable.class).from("storage-a").id(10).id());
    assertEquals(20, storage.retrieve(Storable.class).from("storage-a").id(20).id());
    assertEquals(30, storage.retrieve(Storable.class).from("storage-a").id(30).id());
  }

  @Test
  public void testRemoveOperation() {
    storage.store(Storable.class).into("storage-a").value(newObject(10));
    storage.store(Storable.class).into("storage-a").value(newObject(20));
    storage.store(Storable.class).into("storage-a").value(newObject(30));

    assertEquals(3, storage.remove(Storable.class).from("storage-a").all().size());

    assertEquals(0, storage.retrieve(Storable.class).from("storage-a").all().size());

    storage.store(Storable.class).into("storage-a").value(newObject(10));
    storage.store(Storable.class).into("storage-a").value(newObject(20));
    storage.store(Storable.class).into("storage-a").value(newObject(30));

    assertEquals(10, storage.remove(Storable.class).from("storage-a").id(10).id());
    assertEquals(20, storage.remove(Storable.class).from("storage-a").id(20).id());
    assertEquals(30, storage.remove(Storable.class).from("storage-a").id(30).id());

    assertEquals(0, storage.retrieve(Storable.class).from("storage-a").all().size());
  }

}
