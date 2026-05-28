package MLM_my1c_project.ADMIN;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import  MLM_my1c_project.Scanner_and_DB_connection;

import static MLM_my1c_project.Scanner_and_DB_connection.sc;

public class commission_plan {

     commission_plan() {

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
                choice = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println(" Invalid input. Enter number only.(1-3)");
                sc.nextLine();
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

        int choice = sc.nextInt();
        sc.nextLine();
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

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

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

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

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

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            String isertSql = "INSERT INTO commission_plan (level, commission_amount) " +
                    "VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE commission_amount=?";

            PreparedStatement ps = con.prepareStatement(isertSql);

            for (int level = 1; level <= 4; level++) {

                int amount;

                while (true) {
                    System.out.print("Enter  referral commission amount for Level " + level + ": ");

                    try {
                        amount = sc.nextInt();
                        sc.nextLine();

                        if (amount < 0 || amount > 500000) {
                            System.out.println(" Enter valid amount (1 - 500000 Rs.only  Radhe)");
                            continue;
                        }
                        break;

                    } catch (Exception e) {
                        System.out.println(" Invalid input. Enter number only.");
                        sc.nextLine();
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

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

            for (int level = 1; level <= 4; level++) {

                System.out.print("Enter Deposit Commission % for Level " + level + ": ");
                double percent;

                try {
                    percent = sc.nextDouble();
                    sc.nextLine(); 
                     if (percent < 0 || percent > 100) {
                            System.out.println(" Enter valid percentage (0-100)");
                            continue;
                        }
                        
                } catch (Exception e) {
                    System.out.println(" Invalid input! Number only");
                    sc.nextLine();
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
    
}
