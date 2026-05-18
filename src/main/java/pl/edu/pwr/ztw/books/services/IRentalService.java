package pl.edu.pwr.ztw.books.services;

import java.util.Collection;
import pl.edu.pwr.ztw.books.model.Rental;

public interface IRentalService {
    Collection<Rental> getAllRentals();

    Rental getRentalById(int id);

    Rental lendBook(int bookId, int readerId);

    void returnBook(int rentalId);

    boolean hasAssociatedRentals(int readerId);

    boolean hasAssociatedBookRentals(int bookId);
}
