package net.ljcomputing.avt.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import net.ljcomputing.avt.domain.Book;
import net.ljcomputing.avt.domain.BookStore;
import net.ljcomputing.avt.service.BookService;

/*-
 * #%L
 * avt
 * %%
 * Copyright (C) 2021 James G. Willmore
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

/**
 * Book service implementation.
 *
 * @author James G. Willmore
 */
public class BookServiceImpl implements BookService {

  /** Instantiates a new abstract faker mock - should remove for production. */
  public BookServiceImpl() {
    super();
  }

  @Override
  public List<Book> getAllBooks() {
    List<Book> result = new ArrayList<>();

    try {
      File file = new File("./store.xml");
      JAXBContext context = JAXBContext.newInstance(BookStore.class);
      Unmarshaller unmarshallerObj = context.createUnmarshaller();
      BookStore store = (BookStore) unmarshallerObj.unmarshal(file);
      result = store.getBook();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return result;
  }
}
