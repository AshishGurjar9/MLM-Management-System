   
   
package MLM_my1c_project.login_menu;

import static MLM_my1c_project.Scanner_and_DB_connection.sc;

import java.sql.Connection; 

import java.sql.PreparedStatement;
import java.sql.ResultSet;


import MLM_my1c_project.Scanner_and_DB_connection;
import MLM_my1c_project.ADMIN.admin_board;
import MLM_my1c_project.USER.user_board;
public class login_menu {
    public void loginMenu() {

        System.out.println("\n------------------------------");
        System.out.println("        LOGIN TYPE            ");
        System.out.println("------------------------------");
        System.out.println("1. Admin Login");
        System.out.println("2. User Login");
        System.out.println("3. Back");
        System.out.print("Enter choice: ");

        int choice;
        try {
            choice = sc.nextInt();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println(" Invalid input! Please enter number only (1-3)");
            sc.nextLine();
            loginMenu();
            return;

        }
        if (choice == 1) {
            adminLogin();
        } else if (choice == 2) {
            userLogin();
        } else {

            return;
        }
    }

    // ================= ADMIN LOGIN =================
    public void adminLogin() {

        System.out.print("Enter Admin Email: ");
        String email = sc.nextLine();

        System.out.print("Enter Admin Password: ");
        
        String password = sc.nextLine();
 
        try (Connection con = Scanner_and_DB_connection.getConnection()) {
 
            String sql = "SELECT role FROM register WHERE email=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println(" Invalid Admin imformation");
                loginMenu();
                return;
            }

            if (!rs.getString("role").equalsIgnoreCase("ADMIN")) {
                System.out.println(" user can not login as Admin");
                return;
            }

            System.out.println(" Admin Login Successful");
           new admin_board().adminDashboard();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ================= USER LOGIN =================
    public void userLogin() {

        System.out.print("Enter User Email: ");
        String email = sc.nextLine();

        System.out.print("Enter User Password: ");
        String password = sc.nextLine();

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            String sql = "SELECT user_id, role, status, referral_code FROM register WHERE email=? AND password=?";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("  Invalid user imformation");
                loginMenu();
                return;
            }

            String role = rs.getString("role");
            String status = rs.getString("status");
            String myCode = rs.getString("referral_code");

            if (!role.equalsIgnoreCase("USER")) {
                System.out.println(" Admin cannot login as User");
                return;
            }

            if (status.equalsIgnoreCase("BLOCKED")) {
                System.out.println(" Your account is BLOCKED by Admin");
                return;
            }

            System.out.println(" User Login Successful");
          
            new user_board().userDashboard(myCode);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
