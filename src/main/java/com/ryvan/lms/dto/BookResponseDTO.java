package com.ryvan.lms.dto;

import com.ryvan.lms.model.AvailabilityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private AvailabilityStatus availabilityStatus;
}