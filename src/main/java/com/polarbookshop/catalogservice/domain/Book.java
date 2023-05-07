package com.polarbookshop.catalogservice.domain;

import lombok.With;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@With
public record Book(
  @Id Long id,
  @NotBlank(message = "ISBN must not be blank.")
  @Pattern(regexp = "(\\d{10}|\\d{13})",
    message = "ISBN must be a valid ISBN-10 or ISBN-13.")
  String isbn,
  @NotBlank(message = "Title must not be blank.")
  String title,
  @NotBlank(message = "Author must not be blank.")
  String author,
  @NotNull(message = "Price must not be null.")
  @PositiveOrZero(message = "Price must be positive or zero.")
  Double price,
  String publisher,
  // Metadata
  @Version
  int version,
  @CreatedDate
  LocalDateTime createdDate,
  @LastModifiedDate
  LocalDateTime lastModifiedDate
) {
  public static Book of(String isbn, String title, String author, Double price, String publisher) {
    return new Book(null, isbn, title, author, price, publisher, 0, null, null);
  }
}
