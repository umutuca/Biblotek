import java.time.LocalDate;

public class Loan {
    private int id;
    private String userName;
    private int bookId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private String bookAuthor;
    private String bookTitle;
    private String bookCategory; // Nytt fält för bokkategori

    public Loan(int id, String userName, int bookId, LocalDate loanDate, LocalDate returnDate, String bookAuthor, String bookTitle, String bookCategory) {
        this.id = id;
        this.userName = userName;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.bookAuthor = bookAuthor;
        this.bookTitle = bookTitle;
        this.bookCategory = bookCategory;
    }

    // Getters
    public int getId() { return id; }
    public String getUserName() { return userName; }
    public int getBookId() { return bookId; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public String getBookAuthor() { return bookAuthor; }
    public String getBookTitle() { return bookTitle; }
    public String getBookCategory() { return bookCategory; } // Ny getter-metod
}
