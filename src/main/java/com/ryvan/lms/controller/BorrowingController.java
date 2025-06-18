package com.ryvan.lms.controller;

import com.ryvan.lms.dto.BorrowingRequestDTO;
import com.ryvan.lms.dto.BorrowingResponseDTO;
import com.ryvan.lms.service.BorrowingService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {
  private final BorrowingService borrowingService;
  
  @Autowired
  public BorrowingController(BorrowingService borrowingService) {
    this.borrowingService = borrowingService;
  }
  
  @PostMapping
  public ResponseEntity<?> borrowBook(@RequestBody BorrowingRequestDTO borrowingRequestDTO) {
    try {
      BorrowingResponseDTO borrowingTransaction = borrowingService.borrowBook(borrowingRequestDTO);
      return new ResponseEntity<>(borrowingTransaction, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return new ResponseEntity<>(new ErrorResponse("An unexpected error occurred: " + e.getMessage()),
        HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @PutMapping("/return/{isbn}")
  public ResponseEntity<BorrowingResponseDTO> returnBook(@PathVariable String isbn) {
    try {
      BorrowingResponseDTO borrowingTransaction = borrowingService.returnBook(isbn);
      return new ResponseEntity<>(borrowingTransaction, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ErrorResponse {
  private String message;
}