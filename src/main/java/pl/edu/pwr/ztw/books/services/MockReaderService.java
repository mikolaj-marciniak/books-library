package pl.edu.pwr.ztw.books.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import pl.edu.pwr.ztw.books.model.Reader;

@Service
public class MockReaderService implements IReaderService {
    private static List<Reader> readersRepo = new ArrayList<>();

    @Autowired
    @Lazy
    private IRentalService rentalService;

    static {
        readersRepo.add(new Reader(0, "Jan", "Kowalski"));
        readersRepo.add(new Reader(1, "Anna", "Nowak"));
        readersRepo.add(new Reader(2, "Piotr", "Zalewski"));
    }

    @Override
    public Collection<Reader> getAllReaders() {
        return readersRepo.stream()
                .sorted(Comparator.comparingInt(Reader::getId))
                .toList();
    }

    @Override
    public Reader getReaderById(int id) {
        return readersRepo.stream().filter(reader -> reader.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void addReader(Reader reader) {
        int newId = readersRepo.stream().mapToInt(Reader::getId).max().orElse(-1) + 1;
        reader = new Reader(newId, reader.getFirstName(), reader.getLastName());
        readersRepo.add(reader);
    }

    @Override
    public void updateReader(int id, Reader reader) {
        readersRepo.removeIf(r -> r.getId() == id);
        reader = new Reader(id, reader.getFirstName(), reader.getLastName());
        readersRepo.add(reader);
    }

    @Override
    public void deleteReader(int id) {
        if (rentalService.hasAssociatedRentals(id)) {
            throw new IllegalStateException("Cannot delete reader. They have associated rentals.");
        }
        readersRepo.removeIf(reader -> reader.getId() == id);
    }
}
