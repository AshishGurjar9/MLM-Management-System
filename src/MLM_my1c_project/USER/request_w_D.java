package MLM_my1c_project.USER;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static MLM_my1c_project.Scanner_and_DB_connection.sc;

import  MLM_my1c_project.Scanner_and_DB_connection;


public class request_w_D {

    public void requestwithdraw(int userId) {

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            // 1. Get wallet balance
            String walletSql = "SELECT balance FROM wallet WHERE user_id = ?";
            PreparedStatement walletPs = con.prepareStatement(walletSql);
            walletPs.setInt(1, userId);
            ResultSet walletRs = walletPs.executeQuery();
  
            if (!walletRs.next()) {
                System.out.println("Wallet not found!");
                return;
            }
 
            double balance = walletRs.getDouble("balance");

            // 2. Get withdrawal amount
            System.out.print("Enter amount to withdraw: ");
            double amount = sc.nextDouble();
            sc.nextLine();

            if (amount <= 0) {
                System.out.println(" Invalid amount!  kuch  bhi    ");
                return;
            }

            if (amount > balance) {
                System.out.println(" Insufficient amount! Your wallet Balance is " + balance + " Rs.");
                return;
            }

            // 3. Insert request into withdrawal table
            String ins_Sql = "INSERT INTO withdrawal (user_id, amount, status) values (?, ?, 'PENDING')";
            PreparedStatement Ps = con.prepareStatement(ins_Sql);
            Ps.setInt(1, userId);
            Ps.setDouble(2, amount);
            
        
            int rows = Ps.executeUpdate();

            if (rows > 0) {
                System.out.println(" Withdrawal request send admin successfully . Amount: " + amount);
                System.out.println(" Waiting for approval   Radhe Radhe");
            } else {
                System.out.println(" Failed to submit withdrawal request.");
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    public void requestdeposit(int userId) {
        System.out.print("Enter amount to deposit: ");
        double amount;
        try {
            amount = sc.nextDouble();
            sc.nextLine();
        } catch (Exception e) {  
            System.out.println(" Invalid amount! number me  : Radhe Radhe  ");
            sc.nextLine();
            return;
        }

        if (amount <= 0) {
            System.out.println(" Invalid amount! kuch  bhi    ");
            return;
        }

        try (Connection con = Scanner_and_DB_connection.getConnection()) {
            String sql = "INSERT INTO deposit(user_id, amount, created_at, status) VALUES (?, ?, current_timestamp, 'PENDING')";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setDouble(2, amount);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println(" Deposit request send admin successful.");
                System.out.println(" Waiting for approval  Radhe Radhe");
            } else {
                System.out.println(" Failed to submit deposit request.");
            }
        } catch (Exception e) {
            System.out.println("Radhe Radhe - khuch galt he :" + e.getMessage());
        }
}
}