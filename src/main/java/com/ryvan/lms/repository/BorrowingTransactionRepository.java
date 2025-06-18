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
  List<BorrowingTransaction> findByBook(Book book);
  
  Optional<BorrowingTransaction> findFirstByBookAndStatusOrderByBorrowDateDesc(Book book, TransactionStatus status);
}