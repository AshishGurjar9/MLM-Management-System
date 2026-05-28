package MLM_my1c_project.ADMIN;

import java.sql.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static MLM_my1c_project.Scanner_and_DB_connection.sc;

import  MLM_my1c_project.Scanner_and_DB_connection;

public class view_wallet_balance {

  view_wallet_balance() {

        while (true) {
            System.out.println("\n======= WALLET & TRANSACTIONS =======");
            System.out.println("1. View specific user wallet & tranction");
            System.out.println("2. View all users wallet balance ");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Please enter number only (1-3)");
                sc.nextLine();
                continue;
            }
            if (choice == 3)
                return;

            if (choice == 1) {
                System.out.print("Enter User ID: ");
                int uid = sc.nextInt();
                sc.nextLine();
                view_oneuser_Tractions(uid); // User-wise
            } else if (choice == 2) {
                view_wallet(); // All users
            } else {
                System.out.println(" Invalid option  enter (1-3)");
            }
        }
    }

    // ================= VIEW ALL USERS WALLET & TRANSACTIONS (ADMIN)

    public void view_wallet() {

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            String sql = "SELECT r.user_id, r.name, w.balance " +
                    "FROM register r " +
                    "LEFT JOIN wallet w ON r.user_id = w.user_id " +
                    "ORDER BY r.user_id ASC";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n========= ALL USERS WALLET =========");
            System.out.println("ID   " + " Name                    " + "Current Balance");

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String name = rs.getString("name");
                double balance = rs.getDouble("balance");

                System.out.println(id + "     " + name + "                         " + balance);
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    // ================= VIEW MY WALLET & TRANSACTIONS (USER) =================
    public void view_oneuser_Tractions(int userId) {

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            // -------------------- 1️ Wallet Balance --------------------
            String walletSql = "SELECT w.balance ,r.name,r.email FROM wallet w join "+ 
            "register r on w.user_id = r.user_id WHERE w.user_id=?";
            PreparedStatement walletPs = con.prepareStatement(walletSql);
            walletPs.setInt(1, userId);
            ResultSet walletRs = walletPs.executeQuery();
            double balance = 0;
            if (walletRs.next())
                balance = walletRs.getDouble("balance");
               String name = walletRs.getString("name");
               String email = walletRs.getString("email");
             System.out.println("\n User Name: " + name + " |    Email: " + email);
            System.out.println("\n Your Wallet Balance: " + balance);

            // -------------------- 2️ Deposits --------------------
            String depositSql = "SELECT amount,status,created_at FROM deposit WHERE user_id=? and status='approved' ORDER BY created_at DESC";
            PreparedStatement depPs = con.prepareStatement(depositSql);
            depPs.setInt(1, userId);
            ResultSet depRs = depPs.executeQuery();
            double totalDeposit = 0;
            System.out.println("\n----------- DEPOSIT -----------");
            System.out.println("Amount    |    Status    |    Date");
            while (depRs.next()) {
                double amt = depRs.getDouble("amount");
                String status = depRs.getString("status");
                String date = depRs.getString("created_at");
                totalDeposit += amt;
                System.out.println(amt + "     |   " + status + "   |   " + date);
            }
            System.out.println("\nTotal Deposits: " + totalDeposit);

            // -------------------- 3️ Withdrawals --------------------
            String withSql = "SELECT amount, status, created_at FROM withdrawal WHERE user_id=? and status='approved' ORDER BY id DESC";
            PreparedStatement withPs = con.prepareStatement(withSql);
            withPs.setInt(1, userId);
            ResultSet withRs = withPs.executeQuery();
            double totalWithdrawal = 0;
            System.out.println("\n----------- WITHDRAW -----------");
            System.out.println("amount     |   status     |  date");
            while (withRs.next()) {
                double amt = withRs.getDouble("amount");
                String status = withRs.getString("status");
                Timestamp date = withRs.getTimestamp("created_at");

                if (status.equalsIgnoreCase("APPROVED"))
                    totalWithdrawal += amt;
                System.out.println(amt + "   |   " + status + "   |   " + date);
            }
            System.out.println("\nTotal Approved Withdraw: " + totalWithdrawal);

            // -------------------- 4️ Commissions --------------------
            String commSql = "SELECT ch.amount, ch.level, r2.name AS fromUser, ch.created_at " +
                    "FROM commission ch " + 
                    "JOIN register r2 ON ch.from_user_id = r2.user_id " +
                    "WHERE ch.user_id=? ORDER BY ch.id DESC";
            PreparedStatement commPs = con.prepareStatement(commSql);
            commPs.setInt(1, userId);
            ResultSet commRs = commPs.executeQuery();
            double totalCommission = 0;
            System.out.println("\n----------- COMMISSION -----------");
            System.out.println("Amount    |    Level    |    From User    |    Date");

            while (commRs.next())
                 {
                double amt = commRs.getDouble("amount");
                int level = commRs.getInt("level");
                String fromUser = commRs.getString("fromUser");
                Timestamp date = commRs.getTimestamp("created_at");
                totalCommission += amt;
                System.out.println(amt + "    |     " + level + "        |       " + fromUser + "    |     " + date);
            }
            System.out.println("\nTotal Commissions: " + totalCommission);

            // -------------------- 5️Summary --------------------
            System.out.println("\n======= overview =======");
            System.out.println("wallet balance: " + balance);
            System.out.println("total Deposits: " + totalDeposit);
            System.out.println("total Withdrawal: " + totalWithdrawal);
            System.out.println("total commissions: " + totalCommission);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
