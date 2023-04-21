package com.polarbookshop.catalogservice.service;

public class BookNotFoundException extends RuntimeException {
  public BookNotFoundException(String isbn) {
    super("Book with ISBN " + isbn + " not found.");
  }
}
