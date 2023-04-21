package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.service.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.service.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RestControllerAdvice
public class BookControllerAdvice {
  @ExceptionHandler(BookNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String bookNotFoundHandler(BookNotFoundException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(BookAlreadyExistsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String bookNotFoundHandler(BookAlreadyExistsException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  Map<String, String> methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getFieldErrors().stream()
      .map(FieldError.class::cast)
      .collect(toMap(FieldError::getField, f -> f.getDefaultMessage() != null ? "" : f.getDefaultMessage()));
  }
}
