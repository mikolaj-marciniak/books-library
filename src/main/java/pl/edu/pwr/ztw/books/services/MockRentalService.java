package pl.edu.pwr.ztw.books.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.pwr.ztw.books.model.Rental;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
public class MockRentalService implements IRentalService {
    private static List<Rental> rentalsRepo = new ArrayList<>();

    @Autowired
    private IBookService bookService;

    @Autowired
    private IReaderService readerService;

    @Override
    public Collection<Rental> getAllRentals() {
        return rentalsRepo.stream()
                .sorted(Comparator.comparingInt(Rental::getId))
                .toList();
    }

    @Override
    public Rental getRentalById(int id) {
        return rentalsRepo.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
    }

    @Override
    public Rental lendBook(int bookId, int readerId) {
        if (bookService.getBookById(bookId) == null) {
            throw new IllegalArgumentException("Book does not exist.");
        }
        if (readerService.getReaderById(readerId) == null) {
            throw new IllegalArgumentException("Reader does not exist.");
        }

        int newId = rentalsRepo.stream().mapToInt(Rental::getId).max().orElse(-1) + 1;
        Rental newRental = new Rental(newId, bookId, readerId);
        rentalsRepo.add(newRental);

        return newRental;
    }

    @Override
    public void returnBook(int rentalId) {
        Rental rental = rentalsRepo.stream()
                .filter(r -> r.getId() == rentalId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Rental not found"));

        if (rental.getReturnDate() != null) {
            throw new IllegalStateException("Rental already returned.");
        }

        rental.setReturnDate(LocalDate.now());
    }

    @Override
    public boolean hasAssociatedRentals(int readerId) {
        return rentalsRepo.stream().anyMatch(r -> r.getReaderId() == readerId);
    }

    @Override
    public boolean hasAssociatedBookRentals(int bookId) {
        return rentalsRepo.stream().anyMatch(r -> r.getBookId() == bookId);
    }
}
