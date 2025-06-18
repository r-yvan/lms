# Library Management System

A Spring Boot application for managing a library's book inventory and borrowing transactions.

## Features

- Create and manage books
- Borrow and return books
- Track borrowing history
- RESTful API for integration with other systems

## Technologies Used

- Spring Boot 3.5.0
- Spring Data JPA
- Hibernate
- PostgreSQL Database
- Maven

## Getting Started

### Prerequisites

- Java 21
- Maven

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application using Maven:
   ```
   ./mvnw spring-boot:run
   ```
4. The application will start on port 8080

### H2 Console

The H2 console is enabled and can be accessed at:
```
http://localhost:8080/h2-console
```

Connection details:
- JDBC URL: `jdbc:h2:mem:lmsdb`
- Username: `sa`
- Password: `password`

## API Endpoints

### Book Management

#### Create a new book
- **URL**: `/api/books`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "title": "Book Title",
    "author": "Author Name",
    "isbn": "1234567890123",
    "availabilityStatus": "AVAILABLE"
  }
  ```
- **Success Response**: `201 Created`

#### Get book details by ISBN
- **URL**: `/api/books/{isbn}`
- **Method**: `GET`
- **Success Response**: `200 OK`
  ```json
  {
    "id": 1,
    "title": "Book Title",
    "author": "Author Name",
    "isbn": "1234567890123",
    "availabilityStatus": "AVAILABLE"
  }
  ```

#### Get book availability by ISBN
- **URL**: `/api/books/{isbn}/availability`
- **Method**: `GET`
- **Success Response**: `200 OK`
  ```
  "AVAILABLE"
  ```
  or
  ```
  "BORROWED"
  ```

### Borrowing Management

#### Borrow a book
- **URL**: `/api/borrowings`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "isbn": "1234567890123",
    "borrowerName": "John Doe",
    "borrowDate": "2023-06-15T10:30:00"
  }
  ```
- **Success Response**: `201 Created`

#### Return a book
- **URL**: `/api/borrowings/return/{isbn}`
- **Method**: `PUT`
- **Success Response**: `200 OK`

## Error Handling

The API returns appropriate HTTP status codes and error messages:

- `400 Bad Request`: Invalid input or business rule violation
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Unexpected server error

## Sample Data

The application is initialized with sample book data for testing purposes:

1. "The Great Gatsby" by F. Scott Fitzgerald (ISBN: 9780743273565)
2. "To Kill a Mockingbird" by Harper Lee (ISBN: 9780061120084)
3. "1984" by George Orwell (ISBN: 9780451524935)