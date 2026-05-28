
package MLM_my1c_project.ADMIN;

import java.sql.*;

import MLM_my1c_project.Scanner_and_DB_connection;

public class viewUsers {
      viewUsers() {

       try (Connection con = Scanner_and_DB_connection.getConnection()) {

            String sql = "SELECT user_id, name, email, referral_code, parent_referral_code, role, status FROM register";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("\n----------- USER LIST -----------");

            while (rs.next()) {
                System.out.println(
                        "___________________________________________________________________________________________________________");
                System.out.println(
                                 "ID: " + rs.getInt("user_id") +
                                " | Name: " + rs.getString("name") +
                                " | Email: " + rs.getString("email") +
                                " | Referral: " + rs.getString("referral_code") +
                                " | Parent: " + rs.getString("parent_referral_code") +
                                " | Role: " + rs.getString("role") +
                                " | Status: " + rs.getString("status"));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
