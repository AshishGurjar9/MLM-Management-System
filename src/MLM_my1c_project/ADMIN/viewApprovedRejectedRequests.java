

    package MLM_my1c_project.ADMIN;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import MLM_my1c_project.Scanner_and_DB_connection;


public class viewApprovedRejectedRequests {
    
     viewApprovedRejectedRequests() {

    try (Connection con =  Scanner_and_DB_connection.getConnection()) {

        // Combined deposit + withdrawal query
        String sql =
            "SELECT id, user_id, amount, status, created_at, 'DEPOSIT' AS type FROM deposit " +
            "WHERE status IN ('APPROVED', 'REJECTED') " +
            "UNION ALL " +
            "SELECT id, user_id, amount, status, created_at, 'WITHDRAW' AS type FROM withdrawal " +
            "WHERE status IN ('APPROVED', 'REJECTED') " +
            "ORDER BY created_at DESC";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n====== APPROVED/REJECTED REQUESTS ======");
        System.out.println("ID | NAME | EMAIL | TYPE | AMOUNT | STATUS | DATE");

        boolean found = false;

        while (rs.next()) {
            found = true;

            //  data udana user detail from register table
            int userId = rs.getInt("user_id");
            PreparedStatement psUser = con.prepareStatement(
                "SELECT name, email FROM register WHERE user_id=?"
            );
            psUser.setInt(1, userId);
            ResultSet rsUser = psUser.executeQuery();
            String name = "", email = "";
            if (rsUser.next()) {
                name = rsUser.getString("name");
                email = rsUser.getString("email");
            }

            System.out.println(
                rs.getInt("id") + " | " +
                name + " | " +
                email + " | " +
                rs.getString("type") + " | " +
                rs.getDouble("amount") + " | " +
                rs.getString("status") + " | " +
                rs.getTimestamp("created_at")
            );
        }

        if (!found) {
            System.out.println("No approved/rejected deposit or withdrawal requests found.");
        }

    } catch (Exception e) {
        System.out.println("Radhe Radhe - Error: " + e.getMessage());
    }
}
}

