package pl.edu.pwr.ztw.books.services;

import java.util.Collection;
import pl.edu.pwr.ztw.books.model.Reader;

public interface IReaderService {
    Collection<Reader> getAllReaders();
    Reader getReaderById(int id);
    void addReader(Reader reader);
    void updateReader(int id, Reader reader);
    void deleteReader(int id);
}
