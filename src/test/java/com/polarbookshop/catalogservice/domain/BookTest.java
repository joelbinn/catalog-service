package com.polarbookshop.catalogservice.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

class BookTest {
  private static Validator validator;

  @BeforeAll
  static void beforeAll() {
    ValidatorFactory factory = javax.validation.Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }


  @Test
  void whenAllFieldsAreCorrectThenValidationSucceeds() {
    // GIVEN
    Book book = new Book("1234567890", "title", "author", "description", 10.0);

    // WHEN/THEN
    assertThat(validator.validate(book)).isEmpty();
  }

  @Test
  void shouldNotAllowBlankIsbn() {
    // GIVEN
    Book book = new Book("", "title", "author", "description", 10.0);

    // WHEN/THEN
    assertThat(validator.validate(book)).extracting("message").containsExactlyInAnyOrder("ISBN must not be blank.","ISBN must be a valid ISBN-10 or ISBN-13.");
  }
}
