// package MLM_my1c_project;
//  import java.sql.*;
// import java.util.Scanner;

// public class Scanner_and_DB_connection {     //   Radhe Radhe
 
//     // ============ DB CONNECTION ============

//     public static final String url = "jdbc:mysql://localhost:3306/MLM";
//     public static final String user = "root";
//     public static final String pass = "Ashish@123";

//     public static Connection getConnection() throws SQLException{
//         return DriverManager.getConnection(url, user, pass);
//     }

//     public int getUserIdByReferralCode(String referralCode) {
//         int userId = 0; //
//         try (Connection con = Scanner_and_DB_connection.getConnection()) { // database connection huaa yah per
//             String sql = "SELECT user_id FROM register WHERE referral_code = ?";
//             PreparedStatement ps = con.prepareStatement(sql);
//             ps.setString(1, referralCode);
//             ResultSet rs = ps.executeQuery();

//             if (rs.next()) {
//                 userId = rs.getInt("user_id");
//             }
//         } catch (Exception e) {
//             System.out.println(e.getStackTrace());
//         }  
//         return userId;
//     }
//     // ============ scanner object banaya  ============
//     public static final Scanner sc =new Scanner(System.in);

    
// }


package MLM_my1c_project;

import java.sql.*;
import java.util.Scanner;

public class Scanner_and_DB_connection {

    // ===== DB CONNECTION =====

    public static final String url =
            "jdbc:mysql://localhost:3306/MLM";

    public static final String user = "root";
    public static final String pass = "Ashish@123";

    public static Connection getConnection() throws SQLException {

        try {

            // JDBC Driver Load
            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {

            System.out.println("MySQL Driver Not Found");
            e.printStackTrace();
        }

        return DriverManager.getConnection(url, user, pass);
    }

    // ===== USER ID GET =====

    public int getUserIdByReferralCode(String referralCode) {

        int userId = 0;

        try (Connection con =
                     Scanner_and_DB_connection.getConnection()) {

            String sql =
                    "SELECT user_id FROM register WHERE referral_code = ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, referralCode);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                userId = rs.getInt("user_id");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return userId;
    }

    // ===== Scanner Object =====

    public static final Scanner sc = new Scanner(System.in);
}