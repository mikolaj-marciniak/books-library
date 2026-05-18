package pl.edu.pwr.ztw.books.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.pwr.ztw.books.model.Author;
import pl.edu.pwr.ztw.books.model.Book;

@Service
public class MockAuthorService implements IAuthorService {
    private static List<Author> authorsRepo = new ArrayList<>();

    static {
        authorsRepo.add(new Author(0, "Jacek", "Dukaj"));
        authorsRepo.add(new Author(1, "Radek", "Rak"));
        authorsRepo.add(new Author(2, "Albert", "Camus"));
    }

    @Autowired
    private IBookService bookService;

    @Autowired
    private IRentalService rentalService;

    @Override
    public Collection<Author> getAllAuthors() {
        return authorsRepo.stream()
                .sorted(Comparator.comparingInt(Author::getId))
                .toList();
    }

    @Override
    public Author getAuthorById(int id) {
        return authorsRepo.stream().filter(author -> author.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void addAuthor(Author author) {
        int newId = authorsRepo.stream().mapToInt(Author::getId).max().orElse(-1) + 1;
        author = new Author(newId, author.getFirstName(), author.getLastName());
        authorsRepo.add(author);
    }

    @Override
    public void updateAuthor(int id, Author author) {
        authorsRepo.removeIf(a -> a.getId() == id);
        author = new Author(id, author.getFirstName(), author.getLastName());
        authorsRepo.add(author);
    }

    @Override
    public void deleteAuthor(int id) {
        boolean hasRentedBooks = bookService.getAllBooksByAuthorId(id).stream()
                .map(Book::getId)
                .anyMatch(rentalService::hasAssociatedBookRentals);

        if (hasRentedBooks) {
            throw new IllegalStateException("Cannot delete author. At least one of their books has associated rentals.");
        }

        authorsRepo.removeIf(author -> author.getId() == id);
        bookService.deleteAllBooksByAuthorId(id);
    }
}
