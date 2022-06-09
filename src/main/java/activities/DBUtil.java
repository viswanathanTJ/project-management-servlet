package activities;

import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Collection;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.mysql.cj.xdevapi.PreparableStatement;

public class DBUtil {
    public static Connection con = null;

    public class DatabaseConnection {
        public static Connection initializeDatabase()
                throws SQLException, ClassNotFoundException {
            String dbDriver = "com.mysql.jdbc.Driver";
            String dbURL = "jdbc:mysql://localhost:3306/";
            String dbName = "test";
            String dbUsername = "root";
            String dbPassword = "";

            Class.forName(dbDriver);
            con = DriverManager.getConnection(dbURL + dbName, dbUsername, dbPassword);
            return con;
        }
    }

    public class Query {
        static PreparedStatement st;
        static ResultSet r1;

        public static String getUserNameByID(int id) {
            try {
                st = con.prepareStatement("select name from user where u_id=?");
                st.setInt(1, id);
                r1 = st.executeQuery();
                if (r1.next())
                    return r1.getString("name");
                else
                    return "admin";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error " + e.getMessage();
            }
        }

        public static String getUserIDByName(String name) {
            PreparedStatement st;
            try {
                st = con.prepareStatement("select u_id from user where name=?");
                st.setString(1, name);
                r1 = st.executeQuery();
                if (r1.next())
                    return r1.getString("u_id");
                else
                    return null;
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error " + e.getMessage();
            }
        }
    }

}
