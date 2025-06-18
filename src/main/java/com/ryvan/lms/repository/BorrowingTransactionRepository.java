package com.ryvan.lms.repository;

import com.ryvan.lms.model.Book;
import com.ryvan.lms.model.BorrowingTransaction;
import com.ryvan.lms.model.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingTransactionRepository extends JpaRepository<BorrowingTransaction, Long> {
  
  /**
   * Find all transactions for a specific book.
   *
   * @param book the book to find transactions for
   * @return a list of transactions for the book
   */
  List<BorrowingTransaction> findByBook(Book book);
  
  /**
   * Find the latest pending transaction for a book.
   *
   * @param book   the book to find the pending transaction for
   * @param status the status to filter by (typically PENDING)
   * @return an Optional containing the transaction if found, or empty if not found
   */
  Optional<BorrowingTransaction> findFirstByBookAndStatusOrderByBorrowDateDesc(Book book, TransactionStatus status);
}