import java.sql.SQLException;
import java.util.Scanner;

public class LibraryApp {
    private static final String ADMIN_PASSWORD = "123123";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();

        System.out.println("Välkommen till biblioteket!");

        while (true) {
            System.out.print("1. Registrera dig\n2. Logga in\n0. Logga ut\nVal: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                // Registrera användare
                System.out.print("Ange användarnamn: ");
                String userName = scanner.nextLine();
                System.out.print("Ange e-post: ");
                String email = scanner.nextLine();
                System.out.print("Ange lösenord: ");
                String password = scanner.nextLine();

                try {
                    userDAO.registerUser(userName, email, password, false); // Endast vanliga användare
                    System.out.println("Registrering lyckades!");
                } catch (SQLException e) {
                    System.out.println("Registrering misslyckades: " + e.getMessage());
                }
            } else if (choice == 2) {
                // Logga in användare
                System.out.print("Ange användarnamn: ");
                String userName = scanner.nextLine();
                System.out.print("Ange lösenord: ");
                String password = scanner.nextLine();

                try {
                    if (userName.equalsIgnoreCase("admin") && password.equals(ADMIN_PASSWORD)) {
                        System.out.println("Inloggning som admin lyckades!");
                        AdminInterface adminInterface = new AdminInterface();
                        adminInterface.showMenu();
                    } else {
                        User user = userDAO.loginUser(userName, password);
                        if (user != null) {
                            System.out.println("Inloggning lyckades!");
                            UserInterface userInterface = new UserInterface();
                            userInterface.showMenu(userName);
                        } else {
                            System.out.println("Inloggning misslyckades: Felaktigt användarnamn eller lösenord.");
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Inloggning misslyckades: " + e.getMessage());
                }
            } else if (choice == 0) {
                break; // Avsluta programmet
            } else {
                System.out.println("Ogiltigt val! Försök igen.");
            }
        }

        scanner.close();
        System.out.println("Tack för besöket!");
    }
}
