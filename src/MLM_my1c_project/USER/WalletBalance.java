package MLM_my1c_project.USER;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import  MLM_my1c_project.Scanner_and_DB_connection;
public class WalletBalance {
     
    WalletBalance(int userId) {

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            String sql = "SELECT balance FROM wallet WHERE user_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n====== WALLET BALANCE ======");
            if (rs.next()) {
                System.out.println("Current Balance :  " + rs.getDouble("balance"));
            } else {
                System.out.println("Wallet not found!");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
        public void viewTransactionHistory(int userId) {

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            String sql = "SELECT transaction_id, transaction_type, amount, level, status, created_at FROM transactions WHERE user_id=? "
                    +
                    "UNION ALL " +
                    "SELECT id, 'DEPOSIT', amount, NULL, status, created_at FROM deposit WHERE user_id=? " +
                    "UNION ALL " +
                    "SELECT id, 'WITHDRAWAL', amount, NULL, status, created_at FROM withdrawal WHERE user_id=? " +
                    "ORDER BY created_at DESC";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            ps.setInt(3, userId);

            ResultSet rs = ps.executeQuery();

            System.out.println("\n====== TRANSACTION HISTORY ======");
            System.out.println("ID|   TYPE  | AMOUNT |LEVEL| STATUS |   DATE");

            boolean found = false;
            while (rs.next()) {
                found = true;
 
                Integer level = rs.getObject("level") == null ? 0 : rs.getInt("level");
 
                System.out.println(
                        rs.getInt("transaction_id") + " | " +
                                rs.getString("transaction_type") + " | " +
                                rs.getDouble("amount") + " | " +
                                level + " | " +
                                rs.getString("status") + " | " +
                                rs.getTimestamp("created_at"));
            }

            if (!found) {
                System.out.println("No transactions found.");
            }

        } catch (Exception e) {
            System.out.println("Radhe Radhe - Error: " + e.getMessage());
        }
    }

}
