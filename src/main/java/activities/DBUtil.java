package activities;

import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DBUtil {

    public class DatabaseConnection {
        public static Connection initializeDatabase()
                throws SQLException, ClassNotFoundException {
            String dbDriver = "com.mysql.jdbc.Driver";
            String dbURL = "jdbc:mysql://localhost:3306/";
            String dbName = "test";
            String dbUsername = "root";
            String dbPassword = "";

            Class.forName(dbDriver);
            return DriverManager.getConnection(dbURL + dbName, dbUsername, dbPassword);
        }
    }
}
