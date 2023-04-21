package com.polarbookshop.catalogservice.service;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
  private final BookRepository bookRepository;

  public Iterable<Book> viewBooklist() {
    return bookRepository.findAll();
  }

  public Book viewBookDetails(String isbn) {
    return bookRepository.findByIsbn(isbn)
      .orElseThrow(() -> new BookNotFoundException(isbn));
  }

  public Book addBookToCatalog(Book book) {
    if (bookRepository.existsByIsbn(book.isbn())) {
      throw new BookAlreadyExistsException(book.isbn());
    }
    return bookRepository.save(book);
  }

  public void removeBookFromCatalog(String isbn) {
    bookRepository.deleteByIsbn(isbn);
  }

  public Book editBookDetails(String isbn, Book book) {
    return bookRepository.findByIsbn(isbn)
      .map(existingBook -> {
        Book editedBook = new Book(
          existingBook.isbn(),
          book.title(),
          book.author(),
          book.description(),
          book.price()
        );
        return bookRepository.save(editedBook);
      })
      .orElseThrow(() -> new BookNotFoundException(isbn));
  }
}
