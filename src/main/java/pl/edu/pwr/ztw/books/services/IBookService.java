package pl.edu.pwr.ztw.books.services;

import java.util.Collection;

import pl.edu.pwr.ztw.books.model.Book;

public interface IBookService {
    public abstract Collection<Book> getAllBooks();

    public abstract Collection<Book> getAllBooksByAuthorId(int authorId);

    public abstract Book getBookById(int id);

    public abstract void addBook(Book book);

    public abstract void updateBook(int id, Book book);

    public abstract void deleteBook(int id);

    public abstract void deleteAllBooksByAuthorId(int authorId);
}
