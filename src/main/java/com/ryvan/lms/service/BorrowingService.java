package com.ryvan.lms.service;

import com.ryvan.lms.dto.BookResponseDTO;
import com.ryvan.lms.dto.BorrowingRequestDTO;
import com.ryvan.lms.dto.BorrowingResponseDTO;
import com.ryvan.lms.model.AvailabilityStatus;
import com.ryvan.lms.model.Book;
import com.ryvan.lms.model.BorrowingTransaction;
import com.ryvan.lms.model.TransactionStatus;
import com.ryvan.lms.repository.BookRepository;
import com.ryvan.lms.repository.BorrowingTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BorrowingService {
  private final BookRepository bookRepository;
  private final BorrowingTransactionRepository borrowingTransactionRepository;
  private final BookService bookService;

  @Autowired
  public BorrowingService(
      BookRepository bookRepository,
      BorrowingTransactionRepository borrowingTransactionRepository,
      BookService bookService) {
    this.bookRepository = bookRepository;
    this.borrowingTransactionRepository = borrowingTransactionRepository;
    this.bookService = bookService;
  }

  /**
   * Create a new borrowing transaction.
   *
   * @param borrowingRequestDTO the borrowing request data
   * @return the created borrowing transaction
   * @throws IllegalArgumentException if the book is not available or not found
   */
  @Transactional
  public BorrowingResponseDTO borrowBook(BorrowingRequestDTO borrowingRequestDTO) {
    if (borrowingRequestDTO.getIsbn() == null || borrowingRequestDTO.getIsbn().trim().isEmpty()) {
      throw new IllegalArgumentException("ISBN cannot be null or empty");
    }
    if (borrowingRequestDTO.getBorrowerName() == null || borrowingRequestDTO.getBorrowerName().trim().isEmpty()) {
      throw new IllegalArgumentException("Borrower name cannot be null or empty");
    }

    Book book = bookRepository.findByIsbn(borrowingRequestDTO.getIsbn())
        .orElseThrow(
            () -> new IllegalArgumentException("Book with ISBN " + borrowingRequestDTO.getIsbn() + " not found"));

    if (book.getAvailabilityStatus() != AvailabilityStatus.AVAILABLE) {
      throw new IllegalArgumentException(
          "Book with ISBN " + borrowingRequestDTO.getIsbn() + " is not available for borrowing");
    }

    BorrowingTransaction transaction = new BorrowingTransaction();
    transaction.setBook(book);
    transaction.setBorrowerName(borrowingRequestDTO.getBorrowerName());
    transaction.setBorrowDate(
        borrowingRequestDTO.getBorrowDate() != null ? borrowingRequestDTO.getBorrowDate() : LocalDateTime.now());
    transaction.setStatus(TransactionStatus.PENDING);

    book.setAvailabilityStatus(AvailabilityStatus.BORROWED);
    bookRepository.save(book);

    BorrowingTransaction savedTransaction = borrowingTransactionRepository.save(transaction);
    return convertToResponseDTO(savedTransaction);
  }

  /**
   * Return a borrowed book.
   *
   * @param isbn the ISBN of the book to return
   * @return the updated borrowing transaction
   * @throws IllegalArgumentException if the book is not found or not borrowed
   */
  @Transactional
  public BorrowingResponseDTO returnBook(String isbn) {
    // Find the book by ISBN
    Book book = bookRepository.findByIsbn(isbn)
        .orElseThrow(() -> new IllegalArgumentException("Book with ISBN " + isbn + " not found"));

    // Check if the book is borrowed
    if (book.getAvailabilityStatus() != AvailabilityStatus.BORROWED) {
      throw new IllegalArgumentException("Book with ISBN " + isbn + " is not currently borrowed");
    }

    // Find the pending transaction for this book
    BorrowingTransaction transaction = borrowingTransactionRepository
        .findFirstByBookAndStatusOrderByBorrowDateDesc(book, TransactionStatus.PENDING)
        .orElseThrow(
            () -> new IllegalArgumentException("No pending borrowing transaction found for book with ISBN " + isbn));

    // Update the transaction
    transaction.setReturnDate(LocalDateTime.now());
    transaction.setStatus(TransactionStatus.RETURNED);

    // Update book availability status
    book.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
    bookRepository.save(book);

    // Save the updated transaction
    BorrowingTransaction updatedTransaction = borrowingTransactionRepository.save(transaction);

    // Return the response DTO
    return convertToResponseDTO(updatedTransaction);
  }

  /**
   * Convert a BorrowingTransaction entity to a BorrowingResponseDTO.
   *
   * @param transaction the borrowing transaction entity
   * @return the borrowing response DTO
   */
  private BorrowingResponseDTO convertToResponseDTO(BorrowingTransaction transaction) {
    BookResponseDTO bookResponseDTO = new BookResponseDTO(
        transaction.getBook().getId(),
        transaction.getBook().getTitle(),
        transaction.getBook().getAuthor(),
        transaction.getBook().getIsbn(),
        transaction.getBook().getAvailabilityStatus());

    return new BorrowingResponseDTO(
        transaction.getId(),
        bookResponseDTO,
        transaction.getBorrowerName(),
        transaction.getBorrowDate(),
        transaction.getReturnDate(),
        transaction.getStatus());
  }
}