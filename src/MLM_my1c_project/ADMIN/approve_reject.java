package MLM_my1c_project.ADMIN;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import MLM_my1c_project.Scanner_and_DB_connection;

import static MLM_my1c_project.Scanner_and_DB_connection.sc;
public class approve_reject {
    
    approve_reject() {

        while (true) {
            System.out.println("\n==============================");
            System.out.println(" APPROVE / REJECT REQUESTS ");
            System.out.println("==============================");
            System.out.println("1. View Pending Withdrawals");
            System.out.println("2. View Pending Deposits");
            System.out.println("3. view all approved & rejects requests ");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Enter number only (1-3)");
                sc.nextLine();
                continue;
            }

            if (choice == 1) {
                approve_withdraw();

            } else if (choice == 2) {
               approve_deposit();
            } 
            
             else if (choice == 3) {
                new  viewApprovedRejectedRequests();
            } 
            
            else if (choice == 4) {
                return;
            } else {
                System.out.println(" Invalid choice!");
            }
        }
    
    }
   public  void approve_withdraw() {
      
        
        try (Connection con = Scanner_and_DB_connection.getConnection()) {
      

            String sql = "SELECT W.id, W.user_id, W.amount, r.name, r.email FROM withdrawal W " +
                    "JOIN register r ON W.user_id = r.user_id WHERE W.status='PENDING'";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean found = false;

            System.out.println("\n--- Pending Withdrawal Requests ---");
            System.out.println("ID| User ID |   Name   | Amount |   Email");

            while (rs.next()) {
                found = true;
                System.out.println(
                        rs.getInt("id") + " | " +
                                rs.getInt("user_id") + " | " +
                                rs.getString("name") + " | " +
                                rs.getInt("amount") + " | " +
                                rs.getString("email"));
            }

            if (!found)  {
                System.out.println(" No pending withdrawal requests");
                return; // yahin se bahar
            }
 
            System.out.println("\n1. Approve Withdrawal");
            System.out.println("2. Reject Withdrawal");
            System.out.println("3. Back");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter Withdrawal ID to APPROVE: ");
                int wid  = sc.nextInt();           //  mn se choda he esme 
                sc.nextLine();
                withdraw_status(wid, "APPROVED");      

            } else if (choice == 2) {
                System.out.print("Enter Withdrawal ID to REJECT: ");
                int wid = sc.nextInt();
                sc.nextLine();
                withdraw_status(wid, "REJECTED");
            }

           

        } catch (Exception e) {
            System.out.println("Radhe Radhe - khuch galt he  :" + e.getMessage());
        }
    }

    public void withdraw_status(int wid, String status) {

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            con.setAutoCommit(false);

            // agar APPROVED hai tabhi wallet update
            if (status.equals("APPROVED")) {

                String getSql = "SELECT user_id, amount FROM withdrawal WHERE id=?";
                PreparedStatement ps1 = con.prepareStatement(getSql);
                ps1.setInt(1, wid); 
                ResultSet rs = ps1.executeQuery();

                if (!rs.next()) {
                    System.out.println(" Invalid Withdrawal ID");
                    return;
                }

                int userId = rs.getInt("user_id");
                double amount = rs.getDouble("amount");

                // wallet se paisa minus
                String walletSql = "UPDATE wallet SET balance = balance - ? WHERE user_id=? ";
                PreparedStatement ps2 = con.prepareStatement(walletSql);
                ps2.setDouble(1, amount);
                ps2.setInt(2, userId);
                ps2.executeUpdate();

                

            // status update (APPROVED / REJECTED)
            String sql = "UPDATE withdrawal SET status=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, wid);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                con.commit();
                System.out.println(" Withdrawal " + status);
            } else {
                System.out.println(" Invalid Withdrawal ID");
            }
        }
        } catch (Exception e) {
            System.out.println("Radhe Radhe - khuch galt he :" + e.getMessage());
        }
        
    }

    public void approve_deposit() {

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            String sql = "SELECT d.id, d.user_id, d.amount, r.name, r.email FROM deposit d " +
                    "JOIN register r ON d .user_id = r.user_id WHERE d.status='PENDING'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean found = false;

            System.out.println("\n--- Pending Deposit Requests ---");
            System.out.println("ID | User ID |  Name  | Amount |  Email ");

            while (rs.next()) {
                found = true;
                System.out.println(
                        rs.getInt("id") + "  | " +
                                rs.getInt("user_id") + "      | " +
                                rs.getString("name") + "    | " +
                                rs.getInt("amount") + " | " +
                                rs.getString("email"));
            }

            if (!found) {
                System.out.println(" No pending deposit requests");
                return;
            }

            System.out.println("\n1. Approve Deposit");
            System.out.println("2. Reject Deposit");
            System.out.println("3. Back");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter Deposit ID to APPROVE: ");
                int did = sc.nextInt();
                sc.nextLine();
                deposit_status(did, "APPROVED");

            } else if (choice == 2) {
                System.out.print("Enter Deposit ID to REJECT: ");
                int did = sc.nextInt();
                sc.nextLine();
                deposit_status(did, "REJECTED");
            }

            

        } catch (Exception e) {
            System.out.println("Radhe Radhe - khuch galt he : " + e.getMessage());
        }
    
}

    public void deposit_status(int did, String status) {

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            con.setAutoCommit(false);

            if (status.equals("APPROVED")) {

                String getSql = "SELECT user_id, amount FROM deposit WHERE id=?";
                PreparedStatement ps1 = con.prepareStatement(getSql);
                ps1.setInt(1, did);
                ResultSet rs = ps1.executeQuery();

                if (!rs.next()) {
                    System.out.println(" Invalid Deposit ID");
                    return;
                }

                int userId = rs.getInt("user_id");
                double amount = rs.getDouble("amount");

                // wallet me paisa add
                String walletSql = "INSERT INTO wallet (user_id, balance) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE balance = balance + ?";
                PreparedStatement ps2 = con.prepareStatement(walletSql);
                ps2.setInt(1, userId);
                ps2.setDouble(2, amount);
                ps2.setDouble(3, amount);
                ps2.executeUpdate();

                // commission dena
                give_deposit_commission(userId, amount);
            }

            // status update
            String sql = "UPDATE deposit SET status=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, did);

            int rows = ps.executeUpdate();

            if (rows > 0) {

                con.commit();
                System.out.println(" Deposit " + status);
            } else {
                System.out.println(" Invalid Deposit ID");
            }

        } catch (Exception e) {
            System.out.println("Radhe Radhe - khuch galt he :" + e.getMessage());
        }
    }

 public void give_deposit_commission(int userId, double depositAmount) {

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            String parentCode = null;

            PreparedStatement ps = con.prepareStatement(
                    "SELECT parent_referral_code FROM register WHERE user_id=?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();   
            if (rs.next())
                parentCode = rs.getString(1 );

            int level = 1;

            while (parentCode != null && level <= 4) {

                PreparedStatement ps2 = con.prepareStatement(
                        "SELECT user_id, parent_referral_code FROM register WHERE referral_code=?");
                ps2.setString(1, parentCode);
                ResultSet rs2 = ps2.executeQuery();
                if (!rs2.next())
                    break;

                int parentUserId = rs2.getInt("user_id");
                parentCode = rs2.getString("parent_referral_code");

                PreparedStatement ps3 = con.prepareStatement(
                        "SELECT deposit_commi_perent FROM commission_plan WHERE level=?");
                ps3.setInt(1, level);
                ResultSet rs3 = ps3.executeQuery();
                if (!rs3.next())
                    break;

                double percent = rs3.getDouble(1);
                double commissionAmount = depositAmount * percent / 100;

                // Wallet
                PreparedStatement w = con.prepareStatement(
                        "UPDATE wallet SET balance = balance + ? WHERE user_id=?");
                w.setDouble(1, commissionAmount);
                w.setInt(2, parentUserId);
                w.executeUpdate();

                // Commission table
                PreparedStatement c = con.prepareStatement(
                        "INSERT INTO commission (user_id, from_user_id, level, amount, commission_type, created_at) " +
                                "VALUES (?,?,?,?,?,NOW())");
                c.setInt(1, parentUserId);
                c.setInt(2, userId);
                c.setInt(3, level);
                c.setDouble(4, commissionAmount);
                c.setString(5, "DEPOSIT");
                c.executeUpdate();

                // Transaction history
                PreparedStatement t = con.prepareStatement(
                        "INSERT INTO transactions (user_id, from_user_id, transaction_type, level, amount, status ) " +
                                "VALUES (?,?,?,?,?,?)");
                t.setInt(1, parentUserId);
                t.setInt(2, userId);
                t.setString(3, "DEPOSIT_COMMI");
                t.setInt(4, level);
                t.setDouble(5, commissionAmount);
                t.setString(6, "APPROVED");
                t.executeUpdate();

                level++;
            }

        } catch (Exception e) {
            System.out.println("Deposit COMI  Radhe Radhe   = " + e.getMessage());
        }
    }

}

