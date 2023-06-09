package com.polarbookshop.catalogservice.demo;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("testdata")
@RequiredArgsConstructor
public class BookDataLoader {
  private final BookRepository bookRepository;

  @EventListener(ApplicationReadyEvent.class)
  public void loadBookTestData() {
    bookRepository.deleteAll();
    val book1 = bookRepository.save(Book.of("1234567891", "Northern Lights",
        "Lyra Silverstar", 9.90, "publisher1")
      .withTagAdded("stuff", "banan")
      .withTagAdded("apa", "kaka"));

    val book2 = bookRepository.save(Book.of("9234567891", "Ice Age",
        "Aurora Borealis", 567.0, "publisher12")
      .withTagAdded("mega", "stuff"));

    val book3 = Book.of("1234567892", "Polar Journey",
        "Iorek Polarson", 12.90, "publisher2")
      .withReferenceTo(book1)
      .withReferenceTo(book2);
    bookRepository.save(book3);
  }
}
