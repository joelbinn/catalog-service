package com.polarbookshop.catalogservice.domain;

import com.polarbookshop.catalogservice.config.DataConfig;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration-test")
class BookRepositoryTest {
  @Autowired
  BookRepository bookRepository;
  @Autowired
  JdbcAggregateTemplate jdbcAggregateTemplate;

  @Test
  void findByIsbn() {
    // GIVEN
    val bookIsbn = "1234567890";
    val book = Book.of(bookIsbn, "Title", "Author", 9.90, "publisher");
    jdbcAggregateTemplate.insert(book);
    // WHEN
    val maybeBook = bookRepository.findByIsbn(bookIsbn);
    // THEN
    assertThat(maybeBook).isPresent().get().extracting(Book::isbn).isEqualTo(book.isbn());
  }
}
