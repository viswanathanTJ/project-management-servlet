package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import queries.Queries;

public class DBUtil {
    public static Connection con = null;

    public class DatabaseConnection {
        public static Connection getDatabase() {
            try {
                if (con == null || con.isClosed()) {
                    String dbDriver = "com.mysql.cj.jdbc.Driver";
                    String dbURL = "jdbc:mysql://localhost:3306/";
                    String dbName = "test";
                    String dbUsername = "root";
                    String dbPassword = "";
                    Class.forName(dbDriver);
                    con = DriverManager.getConnection(dbURL + dbName, dbUsername, dbPassword);
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return con;
        }
    }

    public class Query {
        static PreparedStatement st;
        static ResultSet r1;

        public static String getUserNameByID(int id) {
            try {
                st = con.prepareStatement(Queries.getUserNameByID);
                st.setInt(1, id);
                r1 = st.executeQuery();
                if (r1.next())
                    return r1.getString("name");
                else
                    return "";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error " + e.getMessage();
            }
        }

        public static String getUserIDByName(String name) {
            try {
                st = con.prepareStatement(Queries.getUserIDByName);
                st.setString(1, name);
                r1 = st.executeQuery();
                if (r1.next())
                    return r1.getString("u_id");
                else
                    return "";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error " + e.getMessage();
            }
        }

        public static String getProjectNameByID(String id) {
            try {
                st = con.prepareStatement(Queries.getProjectByID);
                st.setString(1, id);
                r1 = st.executeQuery();
                if (r1.next())
                    return r1.getString("name");
                else
                    return "";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error " + e.getMessage();
            }
        }
    }

}
