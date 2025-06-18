package com.ryvan.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(nullable = false)
  private String title;
  
  @Column(nullable = false)
  private String author;
  
  @Column(unique = true, nullable = false)
  private String isbn;
  
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AvailabilityStatus availabilityStatus = AvailabilityStatus.AVAILABLE;
}