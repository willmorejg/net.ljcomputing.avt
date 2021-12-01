package net.ljcomputing.avt.service.impl;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.ljcomputing.avt.domain.Author;
import net.ljcomputing.avt.domain.Book;
import net.ljcomputing.avt.domain.MailingAddress;
import net.ljcomputing.avt.domain.Publisher;
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
public class MockBookServiceImpl implements BookService {
  /** The locale. */
  protected final Locale locale;

  /** The random service. */
  protected final RandomService randomService;

  /** The faker. */
  protected final Faker faker;

  /** The fake values service. */
  protected final FakeValuesService fakeValuesService;

  /** Instantiates a new abstract faker mock - should remove for production. */
  public MockBookServiceImpl() {
    super();
    locale = new Locale("en-US");
    randomService = new RandomService();
    faker = new Faker(locale);
    fakeValuesService = new FakeValuesService(locale, randomService);
  }

  @Override
  public List<Book> getAllBooks() {
    final List<Book> result = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      final String title = faker.book().title();
      final String isbn = generateIsbn();
      final double price = faker.number().randomDouble(2, 0, 100);
      final int available = faker.number().numberBetween(0, 100);
      final Book book = new Book();
      book.setTitle(title);
      book.setIsbn(isbn);
      book.setPrice(price);
      book.setAvailable(available);
      final Author author = new Author();
      author.setName(faker.name().name());
      book.getAuthor().add(author);
      final Publisher publisher = new Publisher();
      publisher.setName(faker.company().name());
      final MailingAddress mailingAddress = new MailingAddress();
      mailingAddress.setAddress1(faker.address().streetAddress());
      mailingAddress.setCity(faker.address().cityName());
      mailingAddress.setState(faker.address().stateAbbr());
      mailingAddress.setZipCode(faker.address().zipCode());
      publisher.setMailingAddress(mailingAddress);
      book.getPublisher().add(publisher);
      result.add(book);
    }

    return result;
  }

  private String generateIsbn() {
    final String isbnPattern =
        "\\d{1}-\\d{5}-\\d{3}-\\d{1}|\\d{1}-\\d{3}-\\d{5}-\\d{1}|\\d{1}-\\d{2}-\\d{6}-\\d{1}";
    return fakeValuesService.regexify(isbnPattern);
  }
}
