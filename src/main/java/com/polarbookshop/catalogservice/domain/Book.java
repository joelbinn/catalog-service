package com.polarbookshop.catalogservice.domain;

import lombok.With;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@With
public record Book(
  @NotBlank(message = "ISBN must not be blank.")
  @Pattern(regexp = "(\\d{10}|\\d{13})",
    message = "ISBN must be a valid ISBN-10 or ISBN-13.")
  String isbn,
  @NotBlank(message = "Title must not be blank.")
  String title,
  @NotBlank(message = "Author must not be blank.")
  String author,
  @NotBlank(message = "Description must not be blank.")
  String description,
  @NotNull(message = "Price must not be null.")
  @PositiveOrZero(message = "Price must be positive or zero.")
  Double price
) {
}
