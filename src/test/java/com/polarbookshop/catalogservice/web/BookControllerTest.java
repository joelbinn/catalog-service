package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.persistence.InMemoryBookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
  @Autowired
  WebTestClient webTestClient;

  @Autowired
  InMemoryBookRepository bookRepository;

  @AfterEach
  void tearDown() {
    bookRepository.deleteAll();
  }

  @Test
  void itShouldSupportGettingAllBooks() {
    // GIVEN
    Book expectedBook = new Book("1234567890", "Polar Journey", "Theodore Beauregard", "A tale of danger and survival on the icy seas.", 29.90);
    Book expectedBook2 = new Book("0987654321", "Polar Journey 2", "Theodore Beauregard", "A tale of danger and survival on the icy seas.", 29.90);
    bookRepository.save(expectedBook);
    bookRepository.save(expectedBook2);

    // WHEN/THEN
    webTestClient.get()
      .uri("/books")
      .exchange()
      .expectStatus().isOk()
      .expectBody(new ParameterizedTypeReference<List<Book>>() {
      })
      .value(books -> assertThat(books).containsExactlyInAnyOrder(expectedBook, expectedBook2));
  }

  @Test
  void itShouldSupportGettingBookDetails() {
    // GIVEN
    Book expectedBook = new Book("1234567890", "Polar Journey", "Theodore Beauregard", "A tale of danger and survival on the icy seas.", 29.90);
    bookRepository.save(expectedBook);

    // WHEN/THEN
    webTestClient.get()
      .uri("/books/%s".formatted(expectedBook.isbn()))
      .exchange()
      .expectStatus().isOk()
      .expectBody(Book.class)
      .value(actualBook -> assertThat(actualBook).isEqualTo(expectedBook));
  }

  @Test
  void whenGettingANonExisitingISBNANotFoundResponseShallBeGiven() {
    // GIVEN

    // WHEN/THEN
    webTestClient.get()
      .uri("/books/%s".formatted("1234567890"))
      .exchange()
      .expectStatus().isNotFound();
  }

  @Test
  void whenPostingAValidBookItShouldBeCreated() {
    // GIVEN
    Book expectedBook = new Book("1234567890", "Polar Journey", "Theodore Beauregard", "A tale of danger and survival on the icy seas.", 29.90);

    // WHEN/THEN
    webTestClient.post()
      .uri("/books")
      .bodyValue(expectedBook)
      .exchange()
      .expectStatus().isCreated()
      .expectBody(Book.class)
      .value(actualBook -> {
        assertThat(actualBook).usingRecursiveComparison().isEqualTo(expectedBook);
      });
  }

  @Test
  void whenPostingABookWithAlreadyExistingISBNABadRequestResponseShallBeGiven() {
    // GIVEN
    Book expectedBook = new Book("1234567890", "Polar Journey", "Theodore Beauregard", "A tale of danger and survival on the icy seas.", 29.90);
    bookRepository.save(expectedBook);

    // WHEN/THEN
    webTestClient.post()
      .uri("/books")
      .bodyValue(expectedBook)
      .exchange()
      .expectStatus().isBadRequest();
  }

  @Test
  void itShouldSupportRemovingBooks() {
    // GIVEN
    Book expectedBook = new Book("1234567890", "Polar Journey", "Theodore Beauregard", "A tale of danger and survival on the icy seas.", 29.90);
    bookRepository.save(expectedBook);

    // WHEN/THEN
    webTestClient.delete()
      .uri("/books/%s".formatted(expectedBook.isbn()))
      .exchange()
      .expectStatus().isNoContent();

    assertThat(bookRepository.findByIsbn(expectedBook.isbn())).isEmpty();
  }

  @Test
  void itShouldSupportEditingBookDetails() {
    // GIVEN
    Book expectedBook = new Book("1234567890", "Polar Journey", "Theodore Beauregard", "A tale of danger and survival on the icy seas.", 29.90);
    bookRepository.save(expectedBook);
    Book newBook = expectedBook.withTitle("A Polar Journey");
    // WHEN/THEN
    webTestClient.put()
      .uri("/books/%s".formatted(expectedBook.isbn()))
      .bodyValue(newBook)
      .exchange()
      .expectStatus().isOk()
      .expectBody(Book.class)
      .value(actualBook -> assertThat(actualBook.title()).isEqualTo(newBook.title()));

    assertThat(bookRepository.findByIsbn(expectedBook.isbn())).get().extracting(Book::title).isEqualTo(newBook.title());
  }

  @Test
  void itShouldValidateBookDetailsWhenCreating() {
    // GIVEN a book with an invalid ISBN
    Book expectedBook = new Book("123456789", "Polar Journey", "Theodore Beauregard", "A tale of danger and survival on the icy seas.", 29.90);
    // WHEN/THEN
    webTestClient.post()
      .uri("/books")
      .bodyValue(expectedBook)
      .exchange()
      .expectStatus().isBadRequest();
  }

  @Test
  void itShouldValidateBookDetailsWhenEditing() {
    // GIVEN a book with an invalid title
    Book expectedBook = new Book("1234567890", "", "Theodore Beauregard", "A tale of danger and survival on the icy seas.", 29.90);
    bookRepository.save(expectedBook);
    Book newBook = expectedBook.withIsbn("1111");
    // WHEN/THEN
    webTestClient.put()
      .uri("/books/%s".formatted(expectedBook.isbn()))
      .bodyValue(newBook)
      .exchange()
      .expectStatus().isBadRequest();
  }

}
