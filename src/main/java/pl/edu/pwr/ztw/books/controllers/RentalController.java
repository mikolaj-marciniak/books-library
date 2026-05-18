package pl.edu.pwr.ztw.books.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import pl.edu.pwr.ztw.books.dto.ApiMessageResponse;
import pl.edu.pwr.ztw.books.dto.PaginatedResponse;
import pl.edu.pwr.ztw.books.model.Rental;
import pl.edu.pwr.ztw.books.services.IRentalService;
import pl.edu.pwr.ztw.books.utils.PaginationUtils;

@RestController
@RequestMapping("/api/rentals")
class RentalController {
    @Autowired
    private IRentalService rentalService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<Rental>> getAllRentals(
            @RequestParam(name = "page", defaultValue = "0") int page) {
        return new ResponseEntity<>(PaginationUtils.paginate(rentalService.getAllRentals(), page), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Found the rental")
    @ApiResponse(responseCode = "404", description = "Rental not found", content = @Content)
    public ResponseEntity<?> getRentalById(@PathVariable("id") int id) {
        Rental rental = rentalService.getRentalById(id);
        if (rental != null) {
            return new ResponseEntity<>(rental, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiMessageResponse("Rental not found"), HttpStatus.NOT_FOUND);
        }
    }

    static class LendRequest {
        public int bookId;
        public int readerId;
    }

    @PostMapping
    @ApiResponse(responseCode = "201", description = "Book lent successfully", content = @Content(schema = @Schema(implementation = Rental.class)))
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    public ResponseEntity<?> lendBook(@RequestBody LendRequest request) {
        try {
            Rental rental = rentalService.lendBook(request.bookId, request.readerId);
            return new ResponseEntity<>(rental, HttpStatus.CREATED);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(new ApiMessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Book returned successfully")
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    public ResponseEntity<ApiMessageResponse> deleteRental(@PathVariable("id") int id) {
        try {
            rentalService.returnBook(id);
            return new ResponseEntity<>(new ApiMessageResponse("Book returned successfully"), HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(new ApiMessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /*
    @PostMapping("/{id}/return")
    @ApiResponse(responseCode = "200", description = "Book returned successfully")
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
    public ResponseEntity<String> returnBook(@PathVariable("id") int id) {
        try {
            rentalService.returnBook(id);
            return new ResponseEntity<>("Book returned successfully", HttpStatus.OK);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    */
}
