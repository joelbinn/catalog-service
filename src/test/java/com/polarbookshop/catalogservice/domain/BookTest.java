package com.polarbookshop.catalogservice.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {
  private static Validator validator;

  @BeforeAll
  static void beforeAll() {
    ValidatorFactory factory = jakarta.validation.Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }


  @Test
  void whenAllFieldsAreCorrectThenValidationSucceeds() {
    // GIVEN
    Book book = Book.of("1234567890", "title", "author", 10.0, "publisher");

    // WHEN/THEN
    assertThat(validator.validate(book)).isEmpty();
  }

  @Test
  void shouldNotAllowBlankIsbn() {
    // GIVEN
    Book book = Book.of("", "title", "author", 10.0, "publisher");

    // WHEN/THEN
    assertThat(validator.validate(book)).extracting("message").containsExactlyInAnyOrder("ISBN must not be blank.","ISBN must be a valid ISBN-10 or ISBN-13.");
  }
}
