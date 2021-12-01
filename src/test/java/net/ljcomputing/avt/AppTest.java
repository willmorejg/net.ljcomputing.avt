package net.ljcomputing.avt;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import net.ljcomputing.avt.domain.Book;
import net.ljcomputing.avt.domain.BookStore;
import net.ljcomputing.avt.service.BookService;
import net.ljcomputing.avt.service.impl.BookServiceImpl;
import net.ljcomputing.avt.service.impl.MockBookServiceImpl;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
/** Class that executes unit test. */
public class AppTest {
  private static final Logger log = LoggerFactory.getLogger(AppTest.class);
  private static final String storeXml =
      Path.of(System.getProperty("user.dir"), "out", "store.xml").toString();

  @Test
  public void createBookStoreList() {
    boolean result = true;

    try {
      final JAXBContext context = JAXBContext.newInstance(BookStore.class);
      final Marshaller marshallObj = context.createMarshaller();
      marshallObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      final BookStore store = new BookStore();
      final BookService bookService = new MockBookServiceImpl();
      final List<Book> products = bookService.getAllBooks();
      store.getBook().addAll(products);
      marshallObj.marshal(store, new FileOutputStream(storeXml));
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    }

    assertTrue("unable to marshal to XML.", result);
  }

  /** Basic test to merge a Velocity template with a given context and verify the results. */
  @Test
  public void test() {
    boolean result = true;
    final VelocityEngine velocityEngine = new VelocityEngine();
    velocityEngine.setProperty("resource.loader", "file");
    velocityEngine.setProperty(
        "file.resource.loader.class",
        "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
    velocityEngine.setProperty("file.resource.loader.path", "src/main/webapp/WEB-INF/templates");

    final VelocityContext context = new VelocityContext();
    final BookService bookService = new BookServiceImpl();
    final List<Book> products = bookService.getAllBooks();
    log.debug("products: {}", products);
    context.put("products", products);
    context.put("log", log);

    final StringWriter output = new StringWriter();

    final Template template = velocityEngine.getTemplate("index.vm");

    template.merge(context, output);

    log.debug(" template : {}", output);

    final DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = null;
    int mergedAvailableProducts = 0;

    try {
      builder = builderFactory.newDocumentBuilder();
      final Document document =
          builder.parse(new ByteArrayInputStream(output.toString().getBytes()));
      final XPath xPath = XPathFactory.newInstance().newXPath();
      final String expression = "//tr[@name='product']";
      final NodeList nodeList =
          (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
      mergedAvailableProducts = nodeList.getLength();
      for (int i = 0; i < mergedAvailableProducts; i++) {
        log.debug("item 1: {}", nodeList.item(i).getTextContent());
      }
      result = mergedAvailableProducts == products.size();
    } catch (ParserConfigurationException
        | SAXException
        | IOException
        | XPathExpressionException e) {
      e.printStackTrace();
      log.error("FAILED:", e);
      result = false;
    }

    assertTrue(
        "expected "
            + products.size()
            + " prodocts, but got "
            + mergedAvailableProducts
            + " products.",
        result);
  }
}
