package MLM_my1c_project.ADMIN;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import  MLM_my1c_project.Scanner_and_DB_connection;

public class viewUserTree {

     viewUserTree(String myCode) {
        System.out.println("\n========= USER TREE ========="); // sirf yahan
        printUserTree(myCode, "");
    }

    public void printUserTree(String parentCode, String prefix) {

        try (Connection con = Scanner_and_DB_connection.getConnection()) {

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

                System.out.println(prefix + "|-- " + uname + " (" + code + ")");
                printUserTree(code, prefix + "    ");
            }

        } catch (Exception e) {
            System.out.println("Radhe Radhe - khuch galt he :+" + e.getMessage());
        }
    }

    
}
