package com.ryvan.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrowing_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingTransaction {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "book_id", nullable = false)
  private Book book;
  
  @Column(nullable = false)
  private String borrowerName;
  
  @Column(nullable = false)
  private LocalDateTime borrowDate;
  
  private LocalDateTime returnDate;
  
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransactionStatus status = TransactionStatus.PENDING;
}