package com.user.activities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	public static String url = "jdbc:postgresql://localhost/postgres";
    public static String user = "root";
    public static String password = "root";
    public static String driver = "com.mysql.jdbc.Driver";
    public class DatabaseConnection {
        protected static Connection initializeDatabase()
            throws SQLException, ClassNotFoundException
        {
            String dbDriver = "com.mysql.jdbc.Driver";
            String dbURL = "jdbc:mysql://localhost:3306/";
            // Database name to access
            String dbName = "test";
            String dbUsername = "root";
            String dbPassword = "";
      
            Class.forName(dbDriver);
            Connection con = DriverManager.getConnection(dbURL + dbName,
                                                         dbUsername, 
                                                         dbPassword);
            return con;
        }
    }
}
