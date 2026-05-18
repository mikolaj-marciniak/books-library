package pl.edu.pwr.ztw.books.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import pl.edu.pwr.ztw.books.dto.ApiMessageResponse;
import pl.edu.pwr.ztw.books.dto.PaginatedResponse;
import pl.edu.pwr.ztw.books.model.Reader;
import pl.edu.pwr.ztw.books.services.IReaderService;
import pl.edu.pwr.ztw.books.utils.PaginationUtils;
import pl.edu.pwr.ztw.books.utils.RequestBodyParsers;

@RestController
@RequestMapping("/api/readers")
class ReaderController {
    @Autowired
    private IReaderService readerService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<Reader>> getAllReaders(
            @RequestParam(name = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(PaginationUtils.paginate(readerService.getAllReaders(), page), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found the reader")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Reader not found", content = @io.swagger.v3.oas.annotations.media.Content)
    public ResponseEntity<?> getReaderById(@PathVariable("id") int id) {
        Reader reader = readerService.getReaderById(id);
        if (reader != null) {
            return new ResponseEntity<>(reader, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiMessageResponse("Reader not found"), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Reader created successfully")
    public ResponseEntity<ApiMessageResponse> createReader(@RequestBody Map<String, Object> body) {
        try {
            Reader reader = new Reader(
                    0,
                    RequestBodyParsers.getRequiredString(body, "firstName"),
                    RequestBodyParsers.getRequiredString(body, "lastName"));

            readerService.addReader(reader);
            return new ResponseEntity<>(new ApiMessageResponse("Reader created successfully"), HttpStatus.CREATED);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(new ApiMessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiMessageResponse> updateReader(@PathVariable("id") int id, @RequestBody Map<String, Object> body) {
        try {
            Reader reader = new Reader(
                    id,
                    RequestBodyParsers.getRequiredString(body, "firstName"),
                    RequestBodyParsers.getRequiredString(body, "lastName"));

            readerService.updateReader(id, reader);
            return new ResponseEntity<>(new ApiMessageResponse("Reader updated successfully"), HttpStatus.OK);
        } catch (IllegalArgumentException exception) {
            return new ResponseEntity<>(new ApiMessageResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Reader deleted successfully")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Conflict, reader has active rentals", content = @io.swagger.v3.oas.annotations.media.Content)
    public ResponseEntity<ApiMessageResponse> deleteReader(@PathVariable("id") int id) {
        try {
            readerService.deleteReader(id);
            return new ResponseEntity<>(new ApiMessageResponse("Reader deleted successfully"), HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(new ApiMessageResponse(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
}
