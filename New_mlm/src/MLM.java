
//   ================    RADHE RADHE ===============

import java.sql.*;
import java.util.Scanner;
// import java.util.regex.Pattern;
import java.util.Random;

public class MLM{

    Scanner r = new Scanner(System.in);

    String url = "jdbc:mysql://localhost:3306/MLM"; // database address (kis database se connect hona he) hota he radhe
    String user = "root";
    String pass = "Ashish@123";

    public int getUserIdByReferralCode(String referralCode) {
        int userId = 0; //
        // System.out.println("Trying DB Connection...");
        try (Connection con = DriverManager.getConnection(url, user, pass)) { // database connection huaa yah per
            // System.out.println("DB Connection Successful!");
            String sql = "SELECT user_id FROM register WHERE referral_code = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, referralCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return userId;
    }

    // // ================= MAIN METHOD =================
    public static void main(String[] args) {
        MLM app = new MLM();
        
        app.start();
    }

    // public static void main(String[] args) {

    // try {
    //         Class.forName("com.mysql.cj.jdbc.Driver");

    //     Connection con = DriverManager.getConnection(
    //         "jdbc:mysql://localhost:3306/MLM",
    //         "root",
    //         "Ashish@123"
    //     );

    //     System.out.println("DATABASE CONNECTED SUCCESSFULLY 😄");

    //     con.close();

    // } catch (Exception e) {

    //     System.out.println(e);

    // }

// }

    // ================= MAIN MENU =================
    public void start() {
        System.out.println(
                "*************************************************************************************************************************");
        System.out.println("\n                   .......................................");
        System.out.println("                          *      Radhe Radhe      *         ");
        System.out.println("                   .................................ag.....");

        System.out.println("________________________________________________________________________________");
        System.out.println("          WELCOME TO THE MLM (Multi-Level Marketing) MANAGEMENT SYSTEM           ");
        System.out.println("________________________________________________________________________________");

        while (true) {

            System.out.println("\n================================");
            System.out.println("       @@ MLM MAIN MENU @@        ");
            System.out.println("================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int choice;

            try {
                choice = r.nextInt();
                r.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Please enter number only (1-3)");
                r.nextLine();
                continue;
            }

            if (choice == 1) {
                loginMenu();
            } else if (choice == 2) {
                register();
            } else if (choice == 3) {
                System.out.println("Thank you for visiting MLM System");
                System.out.println(" _____________________________________");
                System.out.println("             Radhe Radhe              ");
                System.out.println(" _____________________________________");

                System.exit(0);

            } else {
                System.out.println("Invalid choice! Try again (1-3)");

            }
        }
    }

    // ================= LOGIN TYPE MENU =================
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
            choice = r.nextInt();
            r.nextLine();
        } catch (Exception e) {
            System.out.println(" Invalid input! Please enter number only (1-3)");
            r.nextLine();
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
        String email = r.nextLine();

        System.out.print("Enter Admin Password: ");
        String password = r.nextLine();

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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
            adminDashboard();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ================= USER LOGIN =================
    public void userLogin() {

        System.out.print("Enter User Email: ");
        String email = r.nextLine();

        System.out.print("Enter User Password: ");
        String password = r.nextLine();

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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
            userDashboard(myCode);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ================= Register =================
    public void register() {

        String name;
        String email;
        String password;
        String referralCode;
        String parentReferralCode;

        System.out.print("Enter Name: ");
        name = r.nextLine();

        // ================= Email conditions he  =================
        while (true) {
            System.out.print("Enter Email: ");
            email = r.nextLine();

            String emailRegex = "^[a-z0-9+_.-]+@[a-z0-9.-]+\\.[a-z]{2,}$";
            if (!email.matches(emailRegex)) {
                System.out.println("? Invalid email format. Must be Valid email");
                continue;
            }
            break;
        }

        // ================= PARENT Referral Code  =================
        while (true) {
            System.out.print("Enter Parent Referral Code: ");
            parentReferralCode = r.nextLine().trim();

            if (parentReferralCode.isEmpty()) {
                System.out.println(" Parent Referral Code required");
                continue;
            }

            if (!parentReferralCode.matches("^MLM@\\d+$")) {
                System.out.println("? Invalid format. Format should be MLM@number");
                continue;
            }

            if (!referralExists(parentReferralCode)) {
                System.out.println(" Parent Referral Code not found");
                continue;
            }
            break;
        }

        // ================= AUTO GENERATE REFERRAL CODE =================
        Random random = new Random();

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            while (true) {
                int number = 1+ random.nextInt(1000); 
                referralCode = "MLM@" + number;

                String checkSql = "SELECT referral_code FROM register WHERE referral_code=?";
                PreparedStatement psCheck = con.prepareStatement(checkSql);
                psCheck.setString(1, referralCode);
                ResultSet rs = psCheck.executeQuery();

                if (!rs.next()) {
                    break; // unique mil gaya
                }
            }

            // ================= PASSWORD =================
            System.out.print("Create Password: ");
            password = r.nextLine();

            // ================= INSERT USER =================
            String sql = "INSERT INTO register (name, email, password, referral_code, parent_referral_code, role) " +
                    "VALUES (?, ?, ?, ?, ?, 'USER')";

            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, referralCode);
            ps.setString(5, parentReferralCode);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt(1);
            }

            // ================= WALLET =================
            String walletSql = "INSERT INTO wallet (user_id, balance) VALUES (?, 0)";
            PreparedStatement psWallet = con.prepareStatement(walletSql);
            psWallet.setInt(1, userId);
            psWallet.executeUpdate();

            give_referral_commission(userId);

            System.out.println("Radhe Radhe :  User   Registation  Successfully ");
            System.out.println("Your Referral Code -  (visible after login)");

        }
         catch (Exception e) {
            System.out.println(" Email already exists ! try again");
        }
    }

    public boolean referralExists(String code) {

        if (code == null || code.isEmpty())
            return false;

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            String sql = "SELECT 1 FROM register WHERE referral_code=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, code);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // true = exist

        } catch (Exception e) {
            return false;
        }
    }

    // ================= ADMIN DASHBOARD =================
    public void adminDashboard() {

        while (true) {

            System.out.println("\n==============================");
            System.out.println("        ADMIN DASHBOARD       ");
            System.out.println("==============================");
            System.out.println("1. View Users");
            System.out.println("2. Block / Manage Users");
            System.out.println("3. Approve / Reject Withdrawa & Deposit");
            System.out.println("4. Set Commission Plan");
            System.out.println("5. Commission Reports");
            System.out.println("6. View Full User Tree");
            System.out.println("7. View all user wallets balances");
            System.out.println("8. Logout");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = r.nextInt();
                r.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Please enter number only (1-7)");
                r.nextLine();
                continue;
            }

            if (choice == 1) {
                viewUsers();
            } else if (choice == 2) {
                manageUsers();
            } else if (choice == 3) {
                approve_reject();
            }

            else if (choice == 4) {
                commission_plan();
            }

            else if (choice == 5) {
                commissionReports();
            }

            else if (choice == 6) {
                viewUserTree(null);
            } else if (choice == 7) {
                view_wallet_balance();
            } else if (choice == 8) {
                System.out.println("Admin Logged Out");
                return;
            } else {
                System.out.println("invalid choice! try again (1-7)");
            }
        }
    }

    // ================= VIEW USERS =================
    public void viewUsers() {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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

    // ================= BLOCK / UNBLOCK USERS =================
    public void manageUsers() {

        while (true) {

            System.out.println("\n=========== USER MANAGEMENT ===========");
            System.out.println("1. Block User");
            System.out.println("2. Unblock User");
            System.out.println("3. View Blocked Users");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = r.nextInt();
                r.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Please enter number only (1-4)");
                r.nextLine();
                continue;
            }
            if (choice == 4)
                return;

            else if (choice == 3) {
                viewBlockedUsers();
                continue;
            } else if (choice != 1 && choice != 2) {
                System.out.println(" Invalid choice! Please enter (1-4)");
                continue;
            }

            int userId;
              System.out.print("Enter User ID: ");
            try {
                userId = r.nextInt();
                r.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Please enter valid User ID");
                r.nextLine();
                continue;
            }

            try (Connection con = DriverManager.getConnection(url, user, pass)) {

                // First check user exists and get current status
          
                String checkSql = "SELECT role, status FROM register WHERE user_id=?";
                PreparedStatement checkPs = con.prepareStatement(checkSql);          
                checkPs.setInt(1, userId);                                         
                ResultSet rs = checkPs.executeQuery();                               

                if (!rs.next()) {
                    System.out.println(" Invalid User ID");
                    continue;
                }

                String role = rs.getString("role");
                String status = rs.getString("status");

                if (role.equalsIgnoreCase("ADMIN")) {
                    System.out.println(" Admin cannot be blocked or unblocked");
                    continue;
                }

                String newStatus = "";

                if (choice == 1) {      // Block    
                    if (status.equalsIgnoreCase("BLOCKED"))  {    
                        System.out.println(" User is already BLOCKED");
                        continue;
                    }
                    newStatus = "BLOCKED";
                }
                 else if (choice == 2) {      // Unblock
                    if (status.equalsIgnoreCase("ACTIVE")) {
                        System.out.println(" User is already ACTIVE");
                        continue;
                    }
                    newStatus = "ACTIVE";
                } else {
                    System.out.println(" Invalid Option");
                    continue;
                }

                // Update status in DB
                String updateSql = "UPDATE register SET status=? WHERE user_id=?";
                PreparedStatement updatePs = con.prepareStatement(updateSql);
                updatePs.setString(1, newStatus);
                updatePs.setInt(2, userId);

                int rows = updatePs.executeUpdate();

                if (rows > 0) {
                    System.out.println(" User status updated to " + newStatus);
                } else {
                    System.out.println(" Something went wrong!");
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // ================= VIEW BLOCKED USERS =================
    public void viewBlockedUsers() {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            String sql = "SELECT user_id, name, email, referral_code, parent_referral_code FROM register WHERE status='BLOCKED'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.println("\n------- BLOCKED USERS LIST -------");

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        "____________________________________________________________________________________________");
                System.out.println(
                        "ID: " + rs.getInt("user_id") +
                                " | Name: " + rs.getString("name") +
                                " | Email: " + rs.getString("email") +
                                " | Referral: " + rs.getString("referral_code") +
                                " | Parent: " + rs.getString("parent_referral_code"));
            }

            if (!found)
                System.out.println(" No Blocked Users Found");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    

    // ================= USER TREE =================5
    public void viewUserTree(String myCode) {
        System.out.println("\n========= USER TREE ========="); // sirf yahan
        printUserTree(myCode, "");
    }

    public void printUserTree(String parentCode, String prefix) {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            String sql;
            if (parentCode == null) {
                sql = "SELECT name, referral_code FROM register WHERE parent_referral_code IS NULL";
            } else {
                sql = "SELECT name, referral_code FROM register WHERE parent_referral_code=?";
            }

            PreparedStatement ps = con.prepareStatement(sql);
            if (parentCode != null)
                ps.setString(1, parentCode);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String code = rs.getString("referral_code");
                String uname = rs.getString("name");

                System.out.println(prefix + "├─- " + uname + " (" + code + ")");
                printUserTree(code, prefix + "│  ");
            }

        } catch (Exception e) {
            System.out.println("Radhe Radhe - khuch galt he :+" + e.getMessage());
        }
    }

    // ================= APPROVE / REJECT WITHDRAWALS =================
    public void approve_reject() {

        while (true) {
            System.out.println("\n==============================");
            System.out.println(" APPROVE / REJECT REQUESTS ");
            System.out.println("==============================");
            System.out.println("1. View Pending Withdrawals");
            System.out.println("2. View Pending Deposits");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = r.nextInt();
                r.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Enter number only (1-3)");
                r.nextLine();
                continue;
            }

            if (choice == 1) {
                approve_withdraw();
            } else if (choice == 2) {
                approve_deposit();
            } 
            
            else if (choice == 3) {
                return;
            } else {
                System.out.println(" Invalid choice!");
            }
        }
    }

    public void approve_withdraw() {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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

            int choice = r.nextInt();
            r.nextLine();

            if (choice == 1) {
                System.out.print("Enter Withdrawal ID to APPROVE: ");
                int wid  = r.nextInt();           //  mn se choda he esme 
                r.nextLine();
                withdraw_status(wid, "APPROVED");      

            } else if (choice == 2) {
                System.out.print("Enter Withdrawal ID to REJECT: ");
                int wid = r.nextInt();
                r.nextLine();
                withdraw_status(wid, "REJECTED");
            }

        } catch (Exception e) {
            System.out.println("Radhe Radhe - khuch galt he  :" + e.getMessage());
        }
    }

    public void withdraw_status(int wid, String status) {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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


            }

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

        } catch (Exception e) {
            System.out.println("Radhe Radhe - khuch galt he :" + e.getMessage());
        }
    }

    public void approve_deposit() {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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

            int choice = r.nextInt();
            r.nextLine();

            if (choice == 1) {
                System.out.print("Enter Deposit ID to APPROVE: ");
                int did = r.nextInt();
                r.nextLine();
                deposit_status(did, "APPROVED");

            } else if (choice == 2) {
                System.out.print("Enter Deposit ID to REJECT: ");
                int did = r.nextInt();
                r.nextLine();
                deposit_status(did, "REJECTED");
            }

          

        } catch (Exception e) {
            System.out.println("Radhe Radhe - khuch galt he : " + e.getMessage());
        }
    }

    public void deposit_status(int did, String status) {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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

    // ================= SET COMMISSION PLAN =================
    public void commission_plan() {

        while (true) {

            System.out.println("\n====================================");
            System.out.println("        SET COMMISSION PLAN");
            System.out.println("====================================");
            System.out.println("1. Set reffral commission Plan");
            System.out.println("2. Set deposit commission Plan");
            System.out.println("3. View Commission Plan");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = r.nextInt();
                r.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input. Enter number only.(1-3)");
                r.nextLine();
                continue;
            }

            if (choice == 1) {
                set_commission_refrral(); 
            } else if (choice == 2) {
                set_commission_deposit(); 
            } else if (choice == 3) {
                view_commission_plan();  
            }
             else if (choice == 4) {
                return;
            } else {
                System.out.println(" Invalid choice! Try again (1-4)");
            }
        }
    }

    public void view_commission_plan() {

        System.out.println("\n==== View Commission Plan ====");
        System.out.println("1. View Referral Commission Plan");
        System.out.println("2. View Deposit Commission Plan ");
        System.out.println("3. Back");
        System.out.print("Enter choice: ");

        int choice = r.nextInt();
        r.nextLine();
        if (choice == 1) {
            commission_refrral();
        } 
        else if (choice == 2) {
            commission_deposit(); 
        } 
        else if (choice == 3) {
            return;
        } 
        else  {
            System.out.println(" Invalid choice! Try again (1-4)");
        }
    }

    public void commission_refrral() {
        System.out.println("\n======= CURRENT REFERRAL COMMISSION PLAN =======");

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            String sql = "SELECT level, commission_amount FROM commission_plan ORDER BY level";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        "Level " + rs.getInt("level") +
                                " = " + rs.getInt("commission_amount") + " Rs.");
            }

            if (!found) {
                System.out.println(" No commission plan set yet.");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        view_commission_plan();
        return;
    }

    public void commission_deposit() {
        System.out.println("\n======= CURRENT DEPOSIT COMMISSION PLAN =======");

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            String sql = "SELECT level, deposit_commi_perent FROM commission_plan ORDER BY level";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                        "Level " + rs.getInt("level") +
                                " = " + rs.getInt("deposit_commi_perent") + " %");
            }

            if (!found) {
                System.out.println(" No commission plan set yet.");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        view_commission_plan();
        return;
    }

    public void set_commission_refrral() {

        System.out.println("\n=================================================");
        System.out.println("     SET / UPDATE referral COMMISSION PLAN");
        System.out.println("              (MAX 4 LEVELS)");
        System.out.println("===================================================");

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            String isertSql = "INSERT INTO commission_plan (level, commission_amount) " +
                    "VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE commission_amount=?";

            PreparedStatement ps = con.prepareStatement(isertSql);

            for (int level = 1; level <= 4; level++) {

                int amount;

                while (true) {
                    System.out.print("Enter  referral commission amount for Level " + level + ": ");

                    try {
                        amount = r.nextInt();
                        r.nextLine();

                        if (amount < 0 || amount > 500000) {
                            System.out.println(" Enter valid amount (1 - 500000 Rs.only  Radhe)");
                            continue;
                        }
                        break;

                    } catch (Exception e) {
                        System.out.println(" Invalid input. Enter number only.");
                        r.nextLine();
                        continue;
                    }
                }

                ps.setInt(1, level) ;
                ps.setInt(2, amount);
                ps.setInt(3, amount);
                ps.executeUpdate();
            }

            System.out.println("\n Commission Plan Saved Successfully");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ========== SET DEPOSIT COMMISSION LEVEL WISE ==========
    public void set_commission_deposit() {

        System.out.println("\n================================================");
        System.out.println("     SET / UPDATE deposit COMMISSION PLAN");
        System.out.println("              (MAX 4 LEVELS)");
        System.out.println("==================================================");

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            for (int level = 1; level <= 4; level++) {

                System.out.print("Enter Deposit Commission % for Level " + level + ": ");
                double percent;

                try {
                    percent = r.nextDouble();
                    r.nextLine(); 
                     if (percent < 0 || percent > 100) {
                            System.out.println(" Enter valid percentage (0-100)");
                            continue;
                        }
                        
                } catch (Exception e) {
                    System.out.println(" Invalid input! Number only");
                    r.nextLine();
                    level--; // same level dobara pooche , ager galat input dali abc jesi to 
                           // ex.  level2 = abc  to exception aayi tolevel 2 phir se poochega
                    continue;
                }

                String sql = "UPDATE commission_plan SET deposit_commi_perent=? WHERE level=?";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setDouble(1, percent);
                ps.setInt(2, level);
                ps.executeUpdate();
            }

            System.out.println(" Deposit Commission Plan Saved (Level Wise)");

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ================= COMMISSION REPORTS =================

    public void commissionReports() {

        while (true) {

            System.out.println("\n======= COMMISSION REPORTS =======");
            System.out.println("1. View All Commission History");
            System.out.println("2. View User-wise Commission");
            System.out.println("3. View Level-wise Commission");
            System.out.println("4. Back");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = r.nextInt();
                r.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input!");
                r.nextLine();
                continue;
            }

            if (choice == 4)
                return;

            try (Connection con = DriverManager.getConnection(url, user, pass)) {

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
                    int uid = r.nextInt();
                    r.nextLine();
                         
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

    public void view_wallet_balance() {

        while (true) {
            System.out.println("\n======= WALLET & TRANSACTIONS =======");
            System.out.println("1. View specific user wallet & tranction");
            System.out.println("2. View all users wallet balance ");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = r.nextInt();
                r.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Please enter number only (1-3)");
                r.nextLine();
                continue;
            }
            if (choice == 3)
                return;

            if (choice == 1) {
                System.out.print("Enter User ID: ");
                int uid = r.nextInt();
                r.nextLine();
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
        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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
            System.out.println("Current wallet balance: " + balance);
            System.out.println("total Deposits: " + totalDeposit);
            System.out.println("total Withdrawal: " + totalWithdrawal);
            System.out.println("total commissions: " + totalCommission);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // ================= USER DASHBOARD =================
    public void userDashboard(String myCode) {

        while (true) {

            System.out.println("\n==============================");
            System.out.println("        USER DASHBOARD        ");
            System.out.println("==============================");
            System.out.println(" Your Referral Code: " + myCode);
            System.out.println("1. View user tree");
            System.out.println("2. Check wallet balance");
            System.out.println("3. Request withdrawal");
            System.out.println("4. Request deposit");
            System.out.println("5. View transction History");
            System.out.println("6. Logout");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = r.nextInt();
                r.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Please enter number only (1-7)");
                r.nextLine();
                continue;
            }
            if (choice == 1) {

                view_user_tree(myCode);
            } else if (choice == 2) {

                int userId = getUserIdByReferralCode(myCode);

                WalletBalance(userId);
            } else if (choice == 3) {

                int userId = getUserIdByReferralCode(myCode);

                requestwithdraw(userId);
            } else if (choice == 4) {

                int userId = getUserIdByReferralCode(myCode);

                requestdeposit(userId);
            }

            else if (choice == 5) {

                int userId = getUserIdByReferralCode(myCode);

                viewTransactionHistory(userId);
            } else if (choice == 6) {
                System.out.println("User Logged Out");
                return;
            } else {
                System.out.println("invalid choice! try again (1-7)");
            }
        }
    }

    public void view_user_tree(String myReferralCode) {

        while (true) {
            System.out.println("\n====== VIEW USER TREE ======");
            System.out.println("1. View Downline");
            System.out.println("2. View Upline");
            System.out.println("3. Back");
            System.out.print("Enter choice: ");

            int choice;
            try {
                choice = r.nextInt();
                r.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input! Please enter number only (1-3)");
                r.nextLine();
                continue;
            }

            if (choice == 3)
                return;

            if (choice == 1) {
                view_downline(myReferralCode);
            } else if (choice == 2) {
                view_upline(myReferralCode);
            } else {
                System.out.println(" Invalid option!");
            }
        }
    }

    // ================= VIEW DOWNLINE =================

    public void view_downline(String myReferralCode) {

        System.out.println("\n========= MY DOWNLINE =========");

        downline(myReferralCode, 1, "");  
    }

    public void downline(String parentCode, int level, String prefix) {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            String sql = "SELECT name, referral_code FROM register " +
                    "WHERE parent_referral_code = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, parentCode);

            ResultSet rs = ps.executeQuery();

            boolean hasChild = false;

            while (rs.next()) {
                hasChild = true;

                String name = rs.getString("name");
                String code = rs.getString("referral_code");

                System.out.println(
                        prefix + "├─ " + name + " (Level " + level + ")"); 

                // recursion (child ke children)
                downline(code, level + 1, prefix + "│   ");
            }

            if (!hasChild && level == 1) {
                System.out.println("No downline found.");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void view_upline(String referralCode) {

        System.out.println("\n========= MY UPLINE =========");
        print_upline(referralCode, "");
    }

    public void print_upline(String referralCode, String prefix) {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            String sql = "SELECT name, parent_referral_code FROM register WHERE referral_code = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, referralCode);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String parentCode = rs.getString("parent_referral_code");

                // Pehle parent call (UPLINE)
                if (parentCode != null) {
                    print_upline(parentCode, prefix + "   ");
                }

                // Phir
                System.out.println("└─ " + name);

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void WalletBalance(int userId) {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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

    // ================= REQUEST WITHDRAWAL =================
    public void requestwithdraw(int userId) {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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
            double amount = r.nextDouble();
            r.nextLine();

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
            amount = r.nextDouble();
            r.nextLine();
        } catch (Exception e) {  
            System.out.println(" Invalid amount! number me  : Radhe Radhe  ");
            r.nextLine();
            return;
        }

        if (amount <= 0) {
            System.out.println(" Invalid amount! kuch  bhi    ");
            return;
        }

        try (Connection con = DriverManager.getConnection(url, user, pass)) {
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

    public void give_referral_commission(int newUserId) {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

            // new user ka parent referral code nikaalo
            String parentSql = "SELECT parent_referral_code FROM register WHERE user_id=?";
            PreparedStatement ps0 = con.prepareStatement(parentSql);
            ps0.setInt(1, newUserId);
            ResultSet rs0 = ps0.executeQuery();

            if (!rs0.next())
                return;

            String parentCode = rs0.getString("parent_referral_code");
            int level = 1;

            while (parentCode != null && level <= 4) {

                // parent user details
                String pSql = "SELECT user_id, parent_referral_code FROM register WHERE referral_code=?";
                PreparedStatement ps1 = con.prepareStatement(pSql);
                ps1.setString(1, parentCode);
                ResultSet rs1 = ps1.executeQuery();

                if (!rs1.next()) 
                    break;

                int parentUserId = rs1.getInt("user_id");
                String nextParentCode = rs1.getString("parent_referral_code");

                // commission amount
                String cSql = "SELECT commission_amount FROM commission_plan WHERE level=?";
                PreparedStatement ps2 = con.prepareStatement(cSql);
                ps2.setInt(1, level);
                ResultSet rs2 = ps2.executeQuery();

                if (!rs2.next())
                    break;

                double commissionAmount = rs2.getDouble("commission_amount");

                // wallet update (auto create wallet)
                String wSql = "INSERT INTO wallet(user_id,balance) VALUES (?,?) " +
                        "ON DUPLICATE KEY UPDATE balance = balance + ?";
                PreparedStatement ps3 = con.prepareStatement(wSql);
                ps3.setInt(1, parentUserId);
                ps3.setDouble(2, commissionAmount);
                ps3.setDouble(3, commissionAmount);
                ps3.executeUpdate();

                // commission table
                String comSql = "INSERT INTO commission(user_id, from_user_id, level, amount, commission_type) VALUES (?,?,?,?,?)";
                PreparedStatement ps4 = con.prepareStatement(comSql);
                ps4.setInt(1, parentUserId);
                ps4.setInt(2, newUserId);
                ps4.setInt(3, level);
                ps4.setDouble(4, commissionAmount);
                ps4.setString(5, "REFERRAL");
                ps4.executeUpdate();

                // transaction history (VERY IMPORTANT)
                String tSql = "INSERT INTO transactions(user_id, from_user_id, transaction_type, amount, level, status) "
                        +
                        "VALUES (?,?,?,?,?,?)";
                PreparedStatement ps5 = con.prepareStatement(tSql);
                ps5.setInt(1, parentUserId);
                ps5.setInt(2, newUserId);
                ps5.setString(3, "Referral_COMMI");
                ps5.setDouble(4, commissionAmount);
                ps5.setInt(5, level);
                ps5.setString(6, "APPROVED");
                ps5.executeUpdate();

                parentCode = nextParentCode;
                level++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void give_deposit_commission(int userId, double depositAmount) {

        try (Connection con = DriverManager.getConnection(url, user, pass)) {

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