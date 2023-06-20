package com.polarbookshop.catalogservice.domain;

import lombok.With;
import lombok.val;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

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
  BigDecimal price,
  String publisher,
  @NotNull(message = "Tags must not be null (but it may be empty).")
  Set<Tag> tags,
  @NotNull(message = "Book references must not be null (but it may be empty).")
  Set<BookReference> bookReferences,
  // Metadata
  @Version
  int version,
  @CreatedDate
  LocalDateTime createdDate,
  @LastModifiedDate
  LocalDateTime lastModifiedDate
) {
  public static Book of(String isbn, String title, String author, Double price, String publisher) {
    return new Book(null, isbn, title, author, Optional.ofNullable(price).map(BigDecimal::valueOf).orElseThrow(), publisher, Set.of(), Set.of(), 0, null, null);
  }

  public Book withTagAdded(String name, String value) {
    val tags = Stream.concat(this.tags.stream(), Stream.of(new Tag(name, value))).collect(toSet());
    return this.withTags(tags);
  }

  public Book withReferenceTo(Book book) {
    return this.withBookReferences(Stream.concat(this.bookReferences.stream(), Stream.of(new BookReference(AggregateReference.to(book.id())))).collect(toSet()));
  }
}
