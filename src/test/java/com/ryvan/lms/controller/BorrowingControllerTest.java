package com.ryvan.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryvan.lms.dto.BookResponseDTO;
import com.ryvan.lms.dto.BorrowingRequestDTO;
import com.ryvan.lms.dto.BorrowingResponseDTO;
import com.ryvan.lms.model.AvailabilityStatus;
import com.ryvan.lms.model.TransactionStatus;
import com.ryvan.lms.service.BorrowingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BorrowingController.class)
public class BorrowingControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private BorrowingService borrowingService;

  @Test
  public void borrowBook_Success() throws Exception {
    // Create request DTO
    BorrowingRequestDTO requestDTO = new BorrowingRequestDTO();
    requestDTO.setIsbn("1234567890");
    requestDTO.setBorrowerName("John Doe");
    requestDTO.setBorrowDate(LocalDateTime.now());

    // Create book response DTO
    BookResponseDTO bookResponseDTO = new BookResponseDTO();
    bookResponseDTO.setId(1L);
    bookResponseDTO.setTitle("Test Book");
    bookResponseDTO.setAuthor("Test Author");
    bookResponseDTO.setIsbn("1234567890");
    bookResponseDTO.setAvailabilityStatus(AvailabilityStatus.BORROWED);

    // Create borrowing response DTO
    BorrowingResponseDTO responseDTO = new BorrowingResponseDTO();
    responseDTO.setId(1L);
    responseDTO.setBook(bookResponseDTO);
    responseDTO.setBorrowerName("John Doe");
    responseDTO.setBorrowDate(requestDTO.getBorrowDate());
    responseDTO.setStatus(TransactionStatus.PENDING);

    when(borrowingService.borrowBook(any(BorrowingRequestDTO.class))).thenReturn(responseDTO);

    mockMvc.perform(post("/api/borrowings")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.book.title").value("Test Book"))
        .andExpect(jsonPath("$.book.author").value("Test Author"))
        .andExpect(jsonPath("$.book.isbn").value("1234567890"))
        .andExpect(jsonPath("$.book.availabilityStatus").value("BORROWED"))
        .andExpect(jsonPath("$.borrowerName").value("John Doe"))
        .andExpect(jsonPath("$.status").value("PENDING"));
  }

  @Test
  public void borrowBook_BadRequest() throws Exception {
    BorrowingRequestDTO requestDTO = new BorrowingRequestDTO();
    requestDTO.setIsbn("nonexistent");
    requestDTO.setBorrowerName("John Doe");
    requestDTO.setBorrowDate(LocalDateTime.now());

    when(borrowingService.borrowBook(any(BorrowingRequestDTO.class)))
        .thenThrow(new IllegalArgumentException("Book not found"));

    mockMvc.perform(post("/api/borrowings")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void returnBook_Success() throws Exception {
    String isbn = "1234567890";

    // Create book response DTO
    BookResponseDTO bookResponseDTO = new BookResponseDTO();
    bookResponseDTO.setId(1L);
    bookResponseDTO.setTitle("Test Book");
    bookResponseDTO.setAuthor("Test Author");
    bookResponseDTO.setIsbn(isbn);
    bookResponseDTO.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

    // Create borrowing response DTO
    BorrowingResponseDTO responseDTO = new BorrowingResponseDTO();
    responseDTO.setId(1L);
    responseDTO.setBook(bookResponseDTO);
    responseDTO.setBorrowerName("John Doe");
    responseDTO.setBorrowDate(LocalDateTime.now().minusDays(1));
    responseDTO.setReturnDate(LocalDateTime.now());
    responseDTO.setStatus(TransactionStatus.RETURNED);

    when(borrowingService.returnBook(isbn)).thenReturn(responseDTO);

    mockMvc.perform(put("/api/borrowings/return/{isbn}", isbn))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.book.title").value("Test Book"))
        .andExpect(jsonPath("$.book.author").value("Test Author"))
        .andExpect(jsonPath("$.book.isbn").value(isbn))
        .andExpect(jsonPath("$.book.availabilityStatus").value("AVAILABLE"))
        .andExpect(jsonPath("$.borrowerName").value("John Doe"))
        .andExpect(jsonPath("$.status").value("RETURNED"));
  }

  @Test
  public void returnBook_BadRequest() throws Exception {
    String isbn = "nonexistent";
    when(borrowingService.returnBook(isbn))
        .thenThrow(new IllegalArgumentException("Book not found"));

    mockMvc.perform(put("/api/borrowings/return/{isbn}", isbn))
        .andExpect(status().isBadRequest());
  }
}