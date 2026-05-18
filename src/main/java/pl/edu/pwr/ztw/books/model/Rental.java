package pl.edu.pwr.ztw.books.model;

import java.time.LocalDate;

public class Rental {
    private int id;
    private int bookId;
    private int readerId;
    private LocalDate loanDate;
    private LocalDate returnDate;

    public Rental() {}

    public Rental(int id, int bookId, int readerId) {
        this.id = id;
        this.bookId = bookId;
        this.readerId = readerId;
        this.loanDate = LocalDate.now();
        this.returnDate = null;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public int getReaderId() { return readerId; }
    public void setReaderId(int readerId) { this.readerId = readerId; }
    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
}
