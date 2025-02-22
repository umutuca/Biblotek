import java.util.Scanner;

public class LibraryApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Välkommen till biblioteket!");
        System.out.print("Ange ditt användarnamn: ");
        String userName = scanner.nextLine();

        if (userName.equalsIgnoreCase("admin")) {
            AdminInterface adminInterface = new AdminInterface();
            adminInterface.showMenu();
        } else {
            UserInterface userInterface = new UserInterface();
            userInterface.showMenu(userName);
        }

        scanner.close();
        System.out.println("Tack för besöket!");
    }
}