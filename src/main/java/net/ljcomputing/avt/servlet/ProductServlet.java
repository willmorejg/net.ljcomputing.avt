package net.ljcomputing.avt.servlet;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.ljcomputing.avt.domain.Book;
import net.ljcomputing.avt.service.BookService;
import net.ljcomputing.avt.service.impl.BookServiceImpl;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityLayoutServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * A servlet to gather products and return a merged Velocity template.
 *
 * @author James G. Willmore
 */
public class ProductServlet extends VelocityLayoutServlet {
  private static final long serialVersionUID = -1420983879361975631L;

  /** A SLF4J Logger member. */
  private static final Logger log = LoggerFactory.getLogger(ProductServlet.class);

  @Override
  public Template handleRequest(
      HttpServletRequest request, HttpServletResponse response, Context context) {
    final BookService bookService = new BookServiceImpl();
    final List<Book> products = bookService.getAllBooks();
    // products.add("Silence of the Lambs");
    // products.add("Red Dragon");
    // products.add("Black Sunday");

    context.put("products", products);
    context.put("title", "Online Book Store");
    context.put("log", LoggerClass.LOG);

    log.debug("getting template");

    return getTemplate("WEB-INF/templates/index.vm");
  }
}
