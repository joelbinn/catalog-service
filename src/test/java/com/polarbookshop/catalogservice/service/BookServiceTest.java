package com.polarbookshop.catalogservice.service;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
  @Mock
  private BookRepository bookRepository;
  @InjectMocks
  private BookService bookService;

  @Test
  void itShouldSupportViewingBooklist() {
    // GIVEN
    var expected = Book.of("1234567890", "Polar Journey", "Theodore Beauregard", 29.90, "publisher");
    when(bookRepository.findAll()).thenReturn(List.of(expected));
    // WHEN
    var actual = bookService.viewBooklist();
    // THEN;
    assertThat(actual).contains(expected);
  }

  @Test
  void itShouldSupportViewingBookDetails() {
    // GIVEN
    var expected = Book.of("1234567890", "Polar Journey", "Theodore Beauregard", 29.90, "publisher");
    when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.of(expected));
    // WHEN
    var actual = bookService.viewBookDetails(expected.isbn());
    // THEN;
    assertThat(actual).isSameAs(expected);
  }

  @Test
  void itShouldFailViewingBookDetailsWhenNotFound() {
    // GIVEN
    when(bookRepository.findByIsbn("1234567890")).thenReturn(Optional.empty());
    // WHEN/THEN
    assertThatThrownBy(() -> bookService.viewBookDetails("1234567890")).isInstanceOf(BookNotFoundException.class);
  }

  @Test
  void itShouldSupportAddingBookToCatalog() {
    // GIVEN
    var book = Book.of("1234567890", "Polar Journey", "Theodore Beauregard", 29.90, "publisher");
    when(bookRepository.existsByIsbn("1234567890")).thenReturn(false);
    when(bookRepository.save(book)).thenReturn(book);
    // WHEN
    var actual = bookService.addBookToCatalog(book);
    // THEN;
    assertThat(actual).isEqualTo(book);
    verify(bookRepository).save(book);
  }

  @Test
  void itShouldFailWhenAddingSameBookMultipleTimesToBookToCatalog() {
    // GIVEN
    var book = Book.of("1234567890", "Polar Journey", "Theodore Beauregard", 29.90, "publisher");
    when(bookRepository.existsByIsbn(book.isbn())).thenReturn(true);
    // WHEN/THEN
    assertThatThrownBy(() -> bookService.addBookToCatalog(book)).isInstanceOf(BookAlreadyExistsException.class);
    // THEN;
    verify(bookRepository, never()).save(book);
  }

  @Test
  void itShouldSupportRemovingBookFromCatalog() {
    // GIVEN
    // WHEN
    bookService.removeBookFromCatalog("1234567890");
    // THEN;
    verify(bookRepository).deleteByIsbn("1234567890");
  }

  @Test
  void itShouldSupportEditingBookDetails() {
    // GIVEN
    var existingBook = Book.of("1234567890", "Polar Journey", "Theodore Beauregard", 29.90, "publisher");
    var newBook = Book.of(existingBook.isbn(), "Polar Journey 2", "Theodore Beauregard 2", 39.90, "publisher");
    when(bookRepository.findByIsbn(existingBook.isbn())).thenReturn(Optional.of(existingBook));
    when(bookRepository.save(newBook)).thenAnswer(invocation -> invocation.getArgument(0));
    // WHEN
    var actual = bookService.editBookDetails(existingBook.isbn(), newBook);
    // THEN;
    assertThat(actual).isEqualTo(newBook);
    verify(bookRepository).save(newBook);
  }

  @Test
  void itShouldFailEditingBookDetailsWhenNotFound() {
    // GIVEN
    var newBook = Book.of("1234567890", "Polar Journey 2", "Theodore Beauregard 2", 39.90, "publisher");
    when(bookRepository.findByIsbn(newBook.isbn())).thenReturn(Optional.empty());
    // WHEN/THEN
    assertThatThrownBy(() -> bookService.editBookDetails(newBook.isbn(), newBook)).isInstanceOf(BookNotFoundException.class);
  }


}
