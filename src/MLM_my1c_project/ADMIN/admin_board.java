package MLM_my1c_project.ADMIN;

import static MLM_my1c_project.Scanner_and_DB_connection.sc;

public class admin_board {


   public void adminDashboard() {

        while (true) {

            System.out.println("\n==============================");
            System.out.println("        ADMIN DASHBOARD       ");
            System.out.println("==============================");
            System.out.println("1. View Users");
            System.out.println("2. Block / Manage Users");
            System.out.println("3. Approve / Reject Withdrawa & Deposit");
            System.out.println("4. Set Commission Plan");
            System.out.println("5. Commission Reports");
            System.out.println("6. View Full User Tree");
            System.out.println("7. View all user wallets balances");
            System.out.println("8. Logout");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Please enter number only (1-7)");
                sc.nextLine();
                continue;
            }

            if (choice == 1) {
                new  viewUsers();
            } else if (choice == 2) {

              new  manageUsers();
              
            } else if (choice == 3) {

                new approve_reject();
            }

            else if (choice == 4) {

              new  commission_plan();
            }

            else if (choice == 5) {
               new  commissionReports();
            }

            else if (choice == 6) {

                new  viewUserTree(null);

            } else if (choice == 7) {

               new  view_wallet_balance();

            } else if (choice == 8) {
                System.out.println("Admin Logged Out");
                return;
            } else {
                System.out.println("invalid choice! try again (1-7)");
            }
        }
    }
}