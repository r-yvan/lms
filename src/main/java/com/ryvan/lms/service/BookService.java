package com.ryvan.lms.service;

import com.ryvan.lms.dto.BookRequestDTO;
import com.ryvan.lms.dto.BookResponseDTO;
import com.ryvan.lms.model.AvailabilityStatus;
import com.ryvan.lms.model.Book;
import com.ryvan.lms.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
  private final BookRepository bookRepository;
  
  @Autowired
  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }
  
  @Transactional
  public BookResponseDTO createBook(BookRequestDTO bookRequestDTO) {
    if (bookRepository.existsByIsbn(bookRequestDTO.getIsbn())) {
      throw new IllegalArgumentException("Book with ISBN " + bookRequestDTO.getIsbn() + " already exists");
    }
    
    Book book = new Book();
    book.setTitle(bookRequestDTO.getTitle());
    book.setAuthor(bookRequestDTO.getAuthor());
    book.setIsbn(bookRequestDTO.getIsbn());
    book.setAvailabilityStatus(bookRequestDTO.getAvailabilityStatus());
    
    Book savedBook = bookRepository.save(book);
    return convertToResponseDTO(savedBook);
  }
  
  @Transactional(readOnly = true)
  public BookResponseDTO getBookByIsbn(String isbn) {
    Book book = bookRepository.findByIsbn(isbn)
      .orElseThrow(() -> new IllegalArgumentException("Book with ISBN " + isbn + " not found"));
    return convertToResponseDTO(book);
  }
  
  @Transactional(readOnly = true)
  public String getBookAvailability(String isbn) {
    Book book = bookRepository.findByIsbn(isbn)
      .orElseThrow(() -> new IllegalArgumentException("Book with ISBN " + isbn + " not found"));
    return book.getAvailabilityStatus().toString();
  }
  
  @Transactional
  public BookResponseDTO updateBookAvailability(String isbn, AvailabilityStatus status) {
    Book book = bookRepository.findByIsbn(isbn)
      .orElseThrow(() -> new IllegalArgumentException("Book with ISBN " + isbn + " not found"));
    book.setAvailabilityStatus(status);
    Book updatedBook = bookRepository.save(book);
    return convertToResponseDTO(updatedBook);
  }
  
  private BookResponseDTO convertToResponseDTO(Book book) {
    return new BookResponseDTO(
      book.getId(),
      book.getTitle(),
      book.getAuthor(),
      book.getIsbn(),
      book.getAvailabilityStatus()
    );
  }
}