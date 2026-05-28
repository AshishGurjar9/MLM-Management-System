package MLM_my1c_project;

import MLM_my1c_project.login_menu.login_menu;
import MLM_my1c_project.Scanner_and_DB_connection;

public class MLM_main {

      public static void main(String[] args) {
        MLM_main app = new MLM_main();
        app.start();
    }

    // ================= MAIN MENU =================
    public void start() {
        System.out.println(
                "*************************************************************************************************************************");
        System.out.println("\n                   .......................................");
        System.out.println("                          *      Radhe Radhe      *         ");
        System.out.println("                   .................................ag.....");

        System.out.println("________________________________________________________________________________");
        System.out.println("          WELCOME TO THE MLM (Multi-Level Marketing) MANAGEMENT SYSTEM           ");
        System.out.println("________________________________________________________________________________");

        while (true) {

            System.out.println("\n================================");
            System.out.println("       @@ MLM MAIN MENU @@        ");
            System.out.println("================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice;

            try {
                choice = Scanner_and_DB_connection.sc.nextInt();

                Scanner_and_DB_connection.sc.nextLine();

            } catch (Exception e) {

                System.out.println(" Invalid input! Please enter number only (1-3)");

                Scanner_and_DB_connection.sc.nextLine();
                continue;
            }

            if (choice == 1) {
              new login_menu().loginMenu();
            } else if (choice == 2) {
                new register();
            } else if (choice == 3) {
                System.out.println("Thank you for visiting MLM System");
                System.out.println(" _____________________________________");
                System.out.println("             Radhe Radhe              ");
                System.out.println(" _____________________________________");

                System.exit(0);

            } else {
                System.out.println("Invalid choice! Try again (1-3)");

            }
        }
    }

}
