# Books Library

Books Library is a simple backend application for managing a library system.  
The project provides REST API endpoints for books, authors, readers and book rentals.

## About the project

This project was created as a Java backend application using Spring Boot.  
It follows a layered structure with separate controllers, services, domain models, DTOs and utility classes.

The application allows managing basic library resources, including:

- books,
- authors,
- readers,
- rentals / book lending.

The current version uses mock service implementations, so the data is stored in memory instead of a database.

## Technologies

- Java 17
- Spring Boot
- Spring Web MVC
- Gradle
- springdoc-openapi / Swagger UI
- JUnit

## Project structure

```text
src/main/java/pl/edu/pwr/ztw/books
├── config
├── controllers
├── dto
├── model
├── services
├── utils
├── BooksApplication.java
└── Hello.java
```
