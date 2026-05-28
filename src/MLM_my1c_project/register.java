package MLM_my1c_project;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import java.sql.Statement;
import static MLM_my1c_project.Scanner_and_DB_connection.sc;




public class register {
    
    register() {

        String name;
        String email;
        String password;
        String referralCode;
        String parentReferralCode;

        System.out.print("Enter Name: ");
        name = sc.nextLine();

        // ================= Email conditions he  =================
        while (true) {
            System.out.print("Enter Email: ");
            email = sc.nextLine();

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
            parentReferralCode = sc.nextLine().trim();

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

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

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
            password = sc.nextLine();

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

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            String sql = "SELECT 1 FROM register WHERE referral_code=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, code);

            ResultSet rs = ps.executeQuery();
            return rs.next(); // true = exist

        } catch (Exception e) {
            return false;
        }
    }
        // ================= GIVE REFERRAL COMMISSION =================


public void give_referral_commission(int newUserId) {

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

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
}