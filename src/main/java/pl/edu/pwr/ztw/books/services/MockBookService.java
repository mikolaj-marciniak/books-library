package pl.edu.pwr.ztw.books.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import pl.edu.pwr.ztw.books.model.Book;

@Service
public class MockBookService implements IBookService {
    private static List<Book> booksRepo = new ArrayList<>();

    @Autowired
    @Lazy
    private IRentalService rentalService;

    static {
        booksRepo.add(new Book(0, "Lód", 0, 999));
        booksRepo.add(new Book(1, "Inne pieśni", 0, 1100));
        booksRepo.add(new Book(2, "Baśń o wężowym sercu", 1, 300));
        booksRepo.add(new Book(3, "Dżuma", 2, 30));
    }

    @Override
    public Collection<Book> getAllBooks() {
        return booksRepo.stream()
                .sorted(Comparator.comparingInt(Book::getId))
                .toList();
    }

    @Override
    public Collection<Book> getAllBooksByAuthorId(int authorId) {
        List<Book> authorBooks = new ArrayList<>();
        for (Book b : booksRepo) {
            if (b.getAuthorId() == authorId) {
                authorBooks.add(b);
            }
        }
        return authorBooks.stream()
                .sorted(Comparator.comparingInt(Book::getId))
                .toList();
    }

    @Override
    public Book getBookById(int id) {
        return booksRepo.stream().filter(book -> book.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void addBook(Book book) {
        int newId = booksRepo.stream().mapToInt(Book::getId).max().orElse(-1) + 1;
        book = new Book(newId, book.getTitle(), book.getAuthorId(), book.getPages());
        booksRepo.add(book);
    }

    @Override
    public void updateBook(int id, Book book) {
        booksRepo.removeIf(b -> b.getId() == id);
        book = new Book(id, book.getTitle(), book.getAuthorId(), book.getPages());
        booksRepo.add(book);
    }

    @Override
    public void deleteBook(int id) {
        if (rentalService.hasAssociatedBookRentals(id)) {
            throw new IllegalStateException("Cannot delete book. It has associated rentals.");
        }
        booksRepo.removeIf(book -> book.getId() == id);
    }

    @Override
    public void deleteAllBooksByAuthorId(int authorId) {
        booksRepo.removeIf(book -> book.getAuthorId() == authorId);
    }
}
