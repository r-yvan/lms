package com.ryvan.lms.controller;

import com.ryvan.lms.dto.BookRequestDTO;
import com.ryvan.lms.dto.BookResponseDTO;
import com.ryvan.lms.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {
  private final BookService bookService;
  
  @Autowired
  public BookController(BookService bookService) {
    this.bookService = bookService;
  }
  
  /**
   * Create a new book.
   *
   * @param bookRequestDTO the book data
   * @return the created book
   */
  @PostMapping
  public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequestDTO bookRequestDTO) {
    try {
      BookResponseDTO createdBook = bookService.createBook(bookRequestDTO);
      return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
  
  /**
   * Get book details by ISBN.
   *
   * @param isbn the ISBN to search for
   * @return the book details
   */
  @GetMapping("/{isbn}")
  public ResponseEntity<BookResponseDTO> getBookByIsbn(@PathVariable String isbn) {
    try {
      BookResponseDTO book = bookService.getBookByIsbn(isbn);
      return new ResponseEntity<>(book, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
  
  /**
   * Get book availability by ISBN.
   *
   * @param isbn the ISBN to check
   * @return the availability status
   */
  @GetMapping("/{isbn}/availability")
  public ResponseEntity<String> getBookAvailability(@PathVariable String isbn) {
    try {
      String availability = bookService.getBookAvailability(isbn);
      return new ResponseEntity<>(availability, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}