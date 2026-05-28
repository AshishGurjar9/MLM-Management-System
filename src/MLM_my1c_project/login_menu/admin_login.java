package MLM_my1c_project.login_menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static MLM_my1c_project.Scanner_and_DB_connection.sc;
import MLM_my1c_project.Scanner_and_DB_connection;
import MLM_my1c_project.ADMIN.admin_board;

public class admin_login {
    
    public void adminLoginPage() {
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
                System.out.println(" Invalid Admin information");
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
}
