package pl.edu.pwr.ztw.books.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Map;
import pl.edu.pwr.ztw.books.dto.ApiMessageResponse;
import pl.edu.pwr.ztw.books.dto.PaginatedResponse;
import pl.edu.pwr.ztw.books.model.Author;
import pl.edu.pwr.ztw.books.services.IAuthorService;
import pl.edu.pwr.ztw.books.utils.PaginationUtils;
import pl.edu.pwr.ztw.books.utils.RequestBodyParsers;

@RestController
class AuthorController {
    @Autowired
    private IAuthorService authorService;

    @GetMapping(value = "/api/authors")
    public ResponseEntity<PaginatedResponse<Author>> getAllAuthors(
            @RequestParam(name = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(PaginationUtils.paginate(authorService.getAllAuthors(), page), HttpStatus.OK);
    }

    @GetMapping(value = "/api/authors/{id}")
    @ApiResponse(responseCode = "200", description = "Found the author")
    @ApiResponse(responseCode = "404", description = "Author not found", content = @io.swagger.v3.oas.annotations.media.Content)
    public ResponseEntity<?> getAuthorById(@PathVariable("id") int id) {
        Author author = authorService.getAuthorById(id);
        if (author != null) {
            return new ResponseEntity<>(author, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiMessageResponse("Author not found"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/api/authors")
    @ApiResponse(responseCode = "201", description = "Author created successfully")
    public ResponseEntity<ApiMessageResponse> createAuthor(@RequestBody Map<String, Object> body) {
        try {
            Author author = new Author(
                    0,
                    RequestBodyParsers.getRequiredString(body, "firstName"),
                    RequestBodyParsers.getRequiredString(body, "lastName"));

            authorService.addAuthor(author);
            return new ResponseEntity<>(new ApiMessageResponse("Author created successfully"), HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(new ApiMessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/api/authors/{id}")
    public ResponseEntity<ApiMessageResponse> updateAuthor(@PathVariable("id") int id, @RequestBody Map<String, Object> body) {
        try {
            Author author = new Author(
                    id,
                    RequestBodyParsers.getRequiredString(body, "firstName"),
                    RequestBodyParsers.getRequiredString(body, "lastName"));

            authorService.updateAuthor(id, author);
            return new ResponseEntity<>(new ApiMessageResponse("Author updated successfully"), HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(new ApiMessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/api/authors/{id}")
    public ResponseEntity<ApiMessageResponse> deleteAuthor(@PathVariable("id") int id) {
        try {
            authorService.deleteAuthor(id);
            return new ResponseEntity<>(new ApiMessageResponse("Author deleted successfully"), HttpStatus.OK);
        } catch (IllegalStateException exception) {
            return new ResponseEntity<>(new ApiMessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
