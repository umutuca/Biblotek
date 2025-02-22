import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;
    private BookDAO bookDAO;
    private LoanDAO loanDAO;

    public UserInterface() {
        scanner = new Scanner(System.in);
        bookDAO = new BookDAO();
        loanDAO = new LoanDAO();
    }

    public void showMenu(String userName) {
        while (true) {
            System.out.println("\n=== ANVÄNDARMENY ===");
            System.out.println("1. Låna bok");
            System.out.println("2. Återlämna bok");
            System.out.println("3. Visa mina lån");
            System.out.println("4. Visa alla böcker");
            System.out.println("0. Logga ut");
            System.out.print("Val: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Rensa bufferten

            switch (choice) {
                case 1:
                    borrowBook(userName);
                    break;
                case 2:
                    returnBook(userName);
                    break;
                case 3:
                    showUserLoans(userName);
                    break;
                case 4:
                    listAllBooks();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Ogiltigt val!");
            }
        }
    }

    private void borrowBook(String userName) {
        try {
            System.out.println("\nTillgängliga böcker:");
            List<Book> books = bookDAO.getAllBooks();
            for (Book book : books) {
                if (book.isAvailable()) {
                    System.out.println(book.getId() + ": " + book.getTitle() + " av " + book.getAuthor());
                }
            }
            System.out.print("Ange bok-ID att låna: ");
            int bookId = scanner.nextInt();
            scanner.nextLine();

            if (bookDAO.isBookAvailable(bookId)) {
                loanDAO.createLoan(userName, bookId);
                bookDAO.updateAvailability(bookId, false);
                System.out.println("Boken är nu lånad!");
            } else {
                System.out.println("Boken är inte tillgänglig!");
            }
        } catch (SQLException e) {
            System.out.println("Databasfel: " + e.getMessage());
        }
    }

    private void returnBook(String userName) {
        try {
            List<Loan> loans = loanDAO.getLoansByUser(userName);
            if (loans.isEmpty()) {
                System.out.println("Du har inga aktiva lån!");
                return;
            }

            System.out.println("\nDina aktiva lån:");
            for (Loan loan : loans) {
                System.out.println("Låne-ID: " + loan.getId() +
                        ", Bok-ID: " + loan.getBookId() +
                        ", Författare: " + loan.getBookAuthor() +
                        ", Bok-titel: " + loan.getBookTitle());
            }

            System.out.print("Ange låne-ID att återlämna: ");
            int loanId = scanner.nextInt();
            scanner.nextLine();

            // Kontrollera om låne-ID finns i listan
            Loan selectedLoan = null;
            for (Loan loan : loans) {
                if (loan.getId() == loanId) {
                    selectedLoan = loan;
                    break;
                }
            }

            if (selectedLoan == null) {
                // Om låne-ID inte hittas, visa ett felmeddelande
                System.out.println("Felaktigt låne-ID, försök igen.");
                return;
            }

            // Om låne-ID är korrekt, fortsätt med återlämning
            loanDAO.returnLoan(loanId);

            // Uppdatera bokens tillgänglighet i databasen
            bookDAO.updateAvailability(selectedLoan.getBookId(), true);

            System.out.println("Boken är nu återlämnad!");
        } catch (SQLException e) {
            System.out.println("Databasfel: " + e.getMessage());
        }
    }


    private void showUserLoans(String userName) {
        try {
            List<Loan> loans = loanDAO.getLoansByUser(userName);
            if (loans.isEmpty()) {
                System.out.println("Du har inga aktiva lån!");
                return;
            }

            System.out.println("\nDina aktiva lån:");
            for (Loan loan : loans) {
                System.out.println("Lånad: " + loan.getLoanDate() +
                        ", Bok-ID: " + loan.getBookId() +
                        ", Författare: " + loan.getBookAuthor() +
                        ", Bok-titel:" + loan.getBookTitle());
            }
        } catch (SQLException e) {
            System.out.println("Databasfel: " + e.getMessage());
        }
    }

    private void listAllBooks() {
        try {
            List<Book> books = bookDAO.getAllBooks();
            System.out.println("\nAlla böcker:");
            for (Book book : books) {
                System.out.println(book.getId() + ": " + book.getTitle() +
                        " av " + book.getAuthor() +
                        " - " + (book.isAvailable() ? "Tillgänglig" : "Utlånad"));
            }
        } catch (SQLException e) {
            System.out.println("Databasfel: " + e.getMessage());
        }
    }
}
