package MLM_my1c_project.ADMIN;

import java.sql.*;

import MLM_my1c_project.Scanner_and_DB_connection;

import static MLM_my1c_project.Scanner_and_DB_connection.sc;

public class manageUsers {
    
     manageUsers() {

        while (true) {

            System.out.println("\n=========== USER MANAGEMENT ===========");
            System.out.println("1. Block User");
            System.out.println("2. Unblock User");
            System.out.println("3. View Blocked Users");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Please enter number only (1-4)");
                sc.nextLine();
                continue;
            }
            if (choice == 4)
                return;

            else if (choice == 3) {
                viewBlockedUsers();
                continue;
            } else if (choice != 1 && choice != 2) {
                System.out.println(" Invalid choice! Please enter (1-4)");
                continue;
            }

            int userId;
              System.out.print("Enter User ID: ");
            try {
                userId = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Please enter valid User ID");
                sc.nextLine();
                continue;
            }
              try (Connection con = Scanner_and_DB_connection.getConnection()) {

                // First check user exists and get current status
          
                String checkSql = "SELECT role, status FROM register WHERE user_id=?";
                PreparedStatement checkPs = con.prepareStatement(checkSql);          
                checkPs.setInt(1, userId);                                         
                ResultSet rs = checkPs.executeQuery();                               

                if (!rs.next()) {
                    System.out.println(" Invalid User ID");
                    continue;
                }

                String role = rs.getString("role");
                String status = rs.getString("status");

                if (role.equalsIgnoreCase("ADMIN")) {
                    System.out.println(" Admin cannot be blocked or unblocked");
                    continue;
                }

                String newStatus = "";

                if (choice == 1) {      // Block    
                    if (status.equalsIgnoreCase("BLOCKED"))  {    
                        System.out.println(" User is already BLOCKED");
                        continue;
                    }
                    newStatus = "BLOCKED";
                }
                 else if (choice == 2) {      // Unblock
                    if (status.equalsIgnoreCase("ACTIVE")) {
                        System.out.println(" User is already ACTIVE");
                        continue;
                    }
                    newStatus = "ACTIVE";
                } else {
                    System.out.println(" Invalid Option");
                    continue;
                }

                // Update status in DB
                String updateSql = "UPDATE register SET status=? WHERE user_id=?";
                PreparedStatement updatePs = con.prepareStatement(updateSql);
                updatePs.setString(1, newStatus);
                updatePs.setInt(2, userId);

                int rows = updatePs.executeUpdate();

                if (rows > 0) {
                    System.out.println(" User status updated to " + newStatus);
                } else {
                    System.out.println(" Something went wrong!");
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // ================= VIEW BLOCKED USERS =================
    public void viewBlockedUsers() {

       try (Connection con = Scanner_and_DB_connection.getConnection()) {

            String sql = "SELECT user_id, name, email, referral_code, parent_referral_code FROM register WHERE status='BLOCKED'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("\n------- BLOCKED USERS LIST -------");

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        "____________________________________________________________________________________________");
                System.out.println(
                        "ID: " + rs.getInt("user_id") +
                                " | Name: " + rs.getString("name") +
                                " | Email: " + rs.getString("email") +
                                " | Referral: " + rs.getString("referral_code") +
                                " | Parent: " + rs.getString("parent_referral_code"));
            }

            if (!found)
                System.out.println(" No Blocked Users Found");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    
}
