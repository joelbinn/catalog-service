package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {
  private final BookService bookService;

  @GetMapping()
  Iterable<Book> getAllBooks() {
    return bookService.viewBooklist();
  }

  @GetMapping("{isbn}")
  Book getBookDetails(@PathVariable String isbn) {
    return bookService.viewBookDetails(isbn);
  }

  @PostMapping()
  @ResponseStatus(HttpStatus.CREATED)
  Book addBook(@Valid @RequestBody Book book) {
    return bookService.addBookToCatalog(book);
  }
  @DeleteMapping("{isbn}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void removeBook(@PathVariable String isbn) {
    bookService.removeBookFromCatalog(isbn);
  }

  @PutMapping("{isbn}")
  Book removeBook(@PathVariable String isbn, @Valid  @RequestBody Book book) {
    return bookService.editBookDetails(isbn, book);
  }
}
