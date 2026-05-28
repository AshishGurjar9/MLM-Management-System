package MLM_my1c_project.USER;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static MLM_my1c_project.Scanner_and_DB_connection.sc;
import  MLM_my1c_project.Scanner_and_DB_connection;

public class view_user_tree {

     view_user_tree(String myReferralCode) {

        while (true) {
            System.out.println("\n====== VIEW USER TREE ======");
            System.out.println("1. View Downline");
            System.out.println("2. View Upline");
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

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

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

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

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
}
