package activities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	
    public class DatabaseConnection {
        protected static Connection initializeDatabase()
            throws SQLException, ClassNotFoundException
        {
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
