package MLM_my1c_project.USER;

import static MLM_my1c_project.Scanner_and_DB_connection.sc;

import MLM_my1c_project.Scanner_and_DB_connection;

public class user_board {
    public void userDashboard(String myCode) {

        Scanner_and_DB_connection get = new Scanner_and_DB_connection();

        while (true) {

            System.out.println("\n==============================");
            System.out.println("        USER DASHBOARD        ");
            System.out.println("==============================");
            System.out.println(" Your Referral Code: " + myCode);
            System.out.println("1. View user tree");
            System.out.println("2. Check wallet balance");
            System.out.println("3. Request withdrawal");
            System.out.println("4. Request deposit");
            System.out.println("5. View transction History");
            System.out.println("6. Logout");
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

               new view_user_tree(myCode);
            } else if (choice == 2) {

                int userId = get.getUserIdByReferralCode(myCode);

               new WalletBalance(userId);
            } else if (choice == 3) {

                int userId = get.getUserIdByReferralCode(myCode);

              new request_w_D().requestwithdraw(userId);
            } else if (choice == 4) {

                int userId = get.getUserIdByReferralCode(myCode);

                new request_w_D().requestdeposit(userId);
            }

            else if (choice == 5) {

                int userId = get.getUserIdByReferralCode(myCode);

                new WalletBalance(userId).viewTransactionHistory(userId);

            } else if (choice == 6) {
                System.out.println("User Logged Out");
                return;
            } else {
                System.out.println("invalid choice! try again (1-7)");
            }
        }
    }

}
