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
        public static String getUserNameByID(int id) {
            PreparedStatement st;
            try {
                st = con.prepareStatement("select name from user where u_id=?");
                st.setInt(1, id);
                ResultSet r1 = st.executeQuery();
                if (r1.next())
                    return r1.getString("name");
                else
                    return "username not found";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error " + e.getMessage();
            }
        }
    }

}
