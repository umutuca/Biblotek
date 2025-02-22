import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class AdminInterface {
    private Scanner scanner;
    private BookDAO bookDAO;
    private LoanDAO loanDAO;

    public AdminInterface() {
        scanner = new Scanner(System.in);
        bookDAO = new BookDAO();
        loanDAO = new LoanDAO();
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== ADMINMENY ===");
            System.out.println("1. Lägg till bok");
            System.out.println("2. Ta bort bok");
            System.out.println("3. Visa alla lån");
            System.out.println("4. Visa alla böcker");
            System.out.println("0. Logga ut");
            System.out.print("Val: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    deleteBook();
                    break;
                case 3:
                    showAllLoans();
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

    private void addBook() {
        System.out.print("Ange titel: ");
        String title = scanner.nextLine();
        System.out.print("Ange författare: ");
        String author = scanner.nextLine();

        try {
            bookDAO.addBook(title, author);
            System.out.println("Bok tillagd!");
        } catch (SQLException e) {
            System.out.println("Databasfel: " + e.getMessage());
        }
    }

    private void deleteBook() {
        try {
            listAllBooks();
            System.out.print("Ange bok-ID att ta bort: ");
            int bookId = scanner.nextInt();
            scanner.nextLine();

            bookDAO.deleteBook(bookId);
            System.out.println("Bok borttagen!");
        } catch (SQLException e) {
            System.out.println("Databasfel: " + e.getMessage());
        }
    }

    private void showAllLoans() {
        try {
            List<Loan> loans = loanDAO.getAllActiveLoans();
            if (loans.isEmpty()) {
                System.out.println("Inga aktiva lån!");
                return;
            }

            System.out.println("\nAktiva lån:");
            for (Loan loan : loans) {
                System.out.println("Låne-ID: " + loan.getId() +
                        ", Användare: " + loan.getUserName() +
                        ", Bok: " + loan.getBookTitle() + " av " + loan.getBookAuthor() + // Uppdaterat
                        ", Lånedatum: " + loan.getLoanDate());
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
