package MLM_my1c_project.ADMIN;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static MLM_my1c_project.Scanner_and_DB_connection.sc;

import  MLM_my1c_project.Scanner_and_DB_connection;

public class commissionReports {
      commissionReports() {

        while (true) {

            System.out.println("\n======= COMMISSION REPORTS =======");
            System.out.println("1. View All Commission History");
            System.out.println("2. View User-wise Commission");
            System.out.println("3. View Level-wise Commission");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input!");
                sc.nextLine();
                continue;
            }

            if (choice == 4)
                return;

            try (Connection con = Scanner_and_DB_connection.getConnection()) {

                PreparedStatement ps;

                // ================= 1. ALL COMMISSION =================
                if (choice == 1) {

                    String sql = "SELECT c.id, r1.name AS receiver, r2.name AS fromUser, " +
                            "c.level, c.amount, c.commission_type AS type, " +
                            "COALESCE(t.created_at, c.created_at) AS created_at FROM commission c " + 
                            "JOIN register r1 ON c.user_id = r1.user_id " +
                            "JOIN register r2 ON c.from_user_id = r2.user_id " +
                            "LEFT JOIN transactions t ON t.user_id = c.user_id AND t.from_user_id = c.from_user_id " +
                            "AND t.level = c.level AND t.transaction_type = 'COMMISSION' ORDER BY created_at DESC;";

                    ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();
                           
                    System.out.println("ID | Receiver | From User | Level | Amount |  Type   | Date");

                    while (rs.next()) {

                        System.out.println(
                                rs.getInt("id") + " | " +
                                        rs.getString("receiver") + "   | " +
                                        rs.getString("fromUser") + "   | " +
                                        rs.getInt("level") + "      | " +
                                        rs.getDouble("amount") + "     | " +
                                        rs.getString("type") + "       | " +
                                        rs.getTimestamp("created_at"));
                    }

                }
 
                // ================= 2. USER WISE (FIXED) =================
                else if (choice == 2) {

                    System.out.print("Enter User ID: ");
                    int uid = sc.nextInt();
                    sc.nextLine();
                         
                    String sql = "SELECT r2.name AS fromUser, c.level, c.amount, c.commission_type AS type," +

                            "COALESCE(t.created_at, c.created_at) AS created_at FROM commission c " +
                            "JOIN register r2 ON c.from_user_id = r2.user_id " +
                            "LEFT JOIN transactions t ON t.user_id = c.user_id AND " +
                            "t.from_user_id = c.from_user_id AND t.level = c.level AND " +
                            "t.transaction_type = 'COMMISSION' WHERE c.user_id=? ORDER BY created_at DESC";

                    ps = con.prepareStatement(sql);
                    ps.setInt(1, uid);
                    ResultSet rs = ps.executeQuery();

                    String nameSql = "SELECT name FROM register WHERE user_id=?";
                    PreparedStatement namePs = con.prepareStatement(nameSql);
                    namePs.setInt(1, uid);
                    ResultSet nameRs = namePs.executeQuery();
                   
                     if (nameRs.next()) {

                      System.out.println("name of user id is :"+  nameRs.getString("name"));
                    } 

                    System.out.println("From User | Level | Amount |  Type  | Date");

                    while (rs.next()) {
                        System.out.println(
                                rs.getString("fromUser") + "         | " +
                                        rs.getInt("level") + "   | " +
                                        rs.getDouble("amount") + "    |   " +
                                        rs.getString("type") + "         | " +
                                        rs.getTimestamp("created_at"));
                    }

                }

                // ================= 3. LEVEL WISE =================
                else if (choice == 3) {

                    String sql = "SELECT level, SUM(amount) AS total_commission, COUNT(*) AS total_tran " +
                            "FROM commission GROUP BY level ORDER BY level";

                    ps = con.prepareStatement(sql);
                    ResultSet rs = ps.executeQuery();

                    System.out.println("Level | Total Commission | Transactions");
                    while (rs.next()) {
                        System.out.println(
                                rs.getInt("level") + "     | " +
                                        rs.getDouble("total_commission") + "           | " +
                                        rs.getInt("total_tran"));
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    
}
