package pl.edu.pwr.ztw.books.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Map;
import pl.edu.pwr.ztw.books.dto.ApiMessageResponse;
import pl.edu.pwr.ztw.books.dto.PaginatedResponse;
import pl.edu.pwr.ztw.books.model.Book;
import pl.edu.pwr.ztw.books.services.IBookService;
import pl.edu.pwr.ztw.books.utils.PaginationUtils;
import pl.edu.pwr.ztw.books.utils.RequestBodyParsers;

@RestController
class BookController {
    @Autowired
    private IBookService bookService;

    @GetMapping("/api/books")
    public ResponseEntity<PaginatedResponse<Book>> getAllBooks(
            @RequestParam(name = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(PaginationUtils.paginate(bookService.getAllBooks(), page), HttpStatus.OK);
    }

    @GetMapping("/api/books/{id}")
    @ApiResponse(responseCode = "200", description = "Found the book")
    @ApiResponse(responseCode = "404", description = "Book not found", content = @io.swagger.v3.oas.annotations.media.Content)
    public ResponseEntity<?> getBookById(@PathVariable("id") int id) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiMessageResponse("Book not found"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/books")
    @ApiResponse(responseCode = "201", description = "Book created successfully")
    public ResponseEntity<ApiMessageResponse> createBook(@RequestBody Map<String, Object> body) {
        try {
            Book book = new Book(
                    0,
                    RequestBodyParsers.getRequiredString(body, "title"),
                    RequestBodyParsers.getRequiredInt(body, "authorId"),
                    RequestBodyParsers.getRequiredInt(body, "pages"));

            bookService.addBook(book);
            return new ResponseEntity<>(new ApiMessageResponse("Book created successfully"), HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(new ApiMessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/books/{id}")
    public ResponseEntity<ApiMessageResponse> updateBook(@PathVariable("id") int id, @RequestBody Map<String, Object> body) {
        try {
            Book book = new Book(
                    id,
                    RequestBodyParsers.getRequiredString(body, "title"),
                    RequestBodyParsers.getRequiredInt(body, "authorId"),
                    RequestBodyParsers.getRequiredInt(body, "pages"));

            bookService.updateBook(id, book);
            return new ResponseEntity<>(new ApiMessageResponse("Book updated successfully"), HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(new ApiMessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/books/{id}")
    public ResponseEntity<ApiMessageResponse> deleteBook(@PathVariable("id") int id) {
        try {
            bookService.deleteBook(id);
            return new ResponseEntity<>(new ApiMessageResponse("Book deleted successfully"), HttpStatus.OK);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(new ApiMessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/authors/{authorId}/books")
    public ResponseEntity<PaginatedResponse<Book>> getAllBooks(
            @PathVariable("authorId") int authorId,
            @RequestParam(name = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(PaginationUtils.paginate(bookService.getAllBooksByAuthorId(authorId), page),
                HttpStatus.OK);
    }

    @GetMapping("/api/authors/{authorId}/books/{id}")
    @ApiResponse(responseCode = "200", description = "Found the book")
    @ApiResponse(responseCode = "404", description = "Book not found", content = @io.swagger.v3.oas.annotations.media.Content)
    public ResponseEntity<?> getBookByAuthorAndId(@PathVariable("authorId") int authorId, @PathVariable("id") int id) {
        Book book = bookService.getBookById(id);
        if (book != null && book.getAuthorId() == authorId) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiMessageResponse("Book not found"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/authors/{authorId}/books")
    @ApiResponse(responseCode = "201", description = "Book created successfully")
    public ResponseEntity<ApiMessageResponse> createBookForAuthor(@PathVariable("authorId") int authorId,
            @RequestBody Map<String, Object> body) {
        try {
            Book book = new Book(
                    0,
                    RequestBodyParsers.getRequiredString(body, "title"),
                    authorId,
                    RequestBodyParsers.getRequiredInt(body, "pages"));

            bookService.addBook(book);
            return new ResponseEntity<>(new ApiMessageResponse("Book created successfully"), HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(new ApiMessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/api/authors/{authorId}/books/{id}")
    public ResponseEntity<ApiMessageResponse> updateBookForAuthor(@PathVariable("authorId") int authorId, @PathVariable("id") int id,
            @RequestBody Map<String, Object> body) {
        try {
            Book book = new Book(
                    id,
                    RequestBodyParsers.getRequiredString(body, "title"),
                    authorId,
                    RequestBodyParsers.getRequiredInt(body, "pages"));

            bookService.updateBook(id, book);
            return new ResponseEntity<>(new ApiMessageResponse("Book updated successfully"), HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(new ApiMessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/authors/{authorId}/books/{id}")
    public ResponseEntity<ApiMessageResponse> deleteBookForAuthor(@PathVariable("authorId") int authorId, @PathVariable("id") int id) {
        try {
            bookService.deleteBook(id);
            return new ResponseEntity<>(new ApiMessageResponse("Book deleted successfully"), HttpStatus.OK);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(new ApiMessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
