package com.polarbookshop.catalogservice.domain;

import lombok.With;
import lombok.val;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
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
  Double price,
  String publisher,
  Set<Tag> tags,
  AggregateReference<Book, Long> referencedBook,
  // Metadata
  @Version
  int version,
  @CreatedDate
  LocalDateTime createdDate,
  @LastModifiedDate
  LocalDateTime lastModifiedDate
) {
  public static Book of(String isbn, String title, String author, Double price, String publisher) {
    return new Book(null, isbn, title, author, price, publisher, Set.of(), null, 0, null, null);
  }

  public Book withTagAdded(String name, String value) {
    val tags = Stream.concat(this.tags.stream(), Stream.of(new Tag(name, value))).collect(toSet());
    return this.withTags(tags);
  }

  public Book withReferenceTo(Book book) {
    return this.withReferencedBook(AggregateReference.to(book.id()));
  }
}
