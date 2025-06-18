package com.ryvan.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryvan.lms.dto.BookRequestDTO;
import com.ryvan.lms.dto.BookResponseDTO;
import com.ryvan.lms.model.AvailabilityStatus;
import com.ryvan.lms.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    public void createBook_Success() throws Exception {
        BookRequestDTO requestDTO = new BookRequestDTO();
        requestDTO.setTitle("Test Book");
        requestDTO.setAuthor("Test Author");
        requestDTO.setIsbn("1234567890");
        requestDTO.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle(requestDTO.getTitle());
        responseDTO.setAuthor(requestDTO.getAuthor());
        responseDTO.setIsbn(requestDTO.getIsbn());
        responseDTO.setAvailabilityStatus(requestDTO.getAvailabilityStatus());

        when(bookService.createBook(any(BookRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.availabilityStatus").value("AVAILABLE"));
    }

    @Test
    public void getBookByIsbn_Success() throws Exception {
        String isbn = "1234567890";
        BookResponseDTO responseDTO = new BookResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Test Book");
        responseDTO.setAuthor("Test Author");
        responseDTO.setIsbn(isbn);
        responseDTO.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        when(bookService.getBookByIsbn(isbn)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/books/{isbn}", isbn))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Book"))
                .andExpect(jsonPath("$.author").value("Test Author"))
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andExpect(jsonPath("$.availabilityStatus").value("AVAILABLE"));
    }

    @Test
    public void getBookByIsbn_NotFound() throws Exception {
        String isbn = "nonexistent";
        when(bookService.getBookByIsbn(isbn)).thenThrow(new IllegalArgumentException("Book not found"));

        mockMvc.perform(get("/api/books/{isbn}", isbn))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBookAvailability_Success() throws Exception {
        String isbn = "1234567890";
        when(bookService.getBookAvailability(isbn)).thenReturn("AVAILABLE");

        mockMvc.perform(get("/api/books/{isbn}/availability", isbn))
                .andExpect(status().isOk())
                .andExpect(content().string("AVAILABLE"));
    }

    @Test
    public void getBookAvailability_NotFound() throws Exception {
        String isbn = "nonexistent";
        when(bookService.getBookAvailability(isbn)).thenThrow(new IllegalArgumentException("Book not found"));

        mockMvc.perform(get("/api/books/{isbn}/availability", isbn))
                .andExpect(status().isNotFound());
    }
} 