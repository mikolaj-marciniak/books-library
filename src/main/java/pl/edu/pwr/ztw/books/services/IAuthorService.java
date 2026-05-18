package pl.edu.pwr.ztw.books.services;

import java.util.Collection;
import pl.edu.pwr.ztw.books.model.Author;

public interface IAuthorService {
    public abstract Collection<Author> getAllAuthors();
    public abstract Author getAuthorById(int id);
    public abstract void addAuthor(Author author);
    public abstract void updateAuthor(int id, Author author);
    public abstract void deleteAuthor(int id);
}
