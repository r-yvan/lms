package com.ryvan.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingRequestDTO {
  private String isbn;
  private String borrowerName;
  private LocalDateTime borrowDate = LocalDateTime.now();
}