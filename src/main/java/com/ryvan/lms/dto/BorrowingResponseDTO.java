package com.ryvan.lms.dto;

import com.ryvan.lms.model.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingResponseDTO {
  private Long id;
  private BookResponseDTO book;
  private String borrowerName;
  private LocalDateTime borrowDate;
  private LocalDateTime returnDate;
  private TransactionStatus status;
}