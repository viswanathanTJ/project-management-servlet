package activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import queries.Queries;
import service.DBUtil.DatabaseConnection;
import service.Hasher;
import service.ResponseHandler;
import service.SessionHandler;

public class Authentication extends ResponseHandler {
    private static final Authentication authentication = new Authentication();

    private Connection con;
    private PreparedStatement st;
    private ResultSet r1;
    public static int interval = 86400;

    private Authentication() {
        this.con = DatabaseConnection.getDatabase();
    }

    public static Authentication getInstance() {
        return authentication;
    }

    public void UserRegister(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = request.getParameter("name");
            String email = request.getParameter("email");
            System.out.println("User Register: " + username + " " + email);

            // Check mail id exists
            st = con.prepareStatement(Queries.getUserByEmail);
            st.setString(1, email);
            r1 = st.executeQuery();
            if (r1.next()) {
                errorResponse(response, 406, "Mail ID already exists.");
                return;
            }

            // Check username exists
            st = con.prepareStatement(Queries.getUserByName);
            st.setString(1, username);
            r1 = st.executeQuery();
            if (r1.next()) {
                errorResponse(response, 406, "Username already exists.");
                return;
            }

            String password = request.getParameter("password");
            // Hash password
            String hashPassword = Hasher.hashPassword(username, password);
            String role = "employee";

            // Set Query
            st = con.prepareStatement(Queries.addUser, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, request.getParameter("name"));
            st.setString(2, request.getParameter("email"));
            st.setString(3, hashPassword);
            st.setString(4, role);

            // Run Query
            int row = st.executeUpdate();
            if (row == 0)
                throw new SQLException("Creating user failed, no rows affected.");

            Long uid = 0L;
            // Store user details in session
            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    uid = generatedKeys.getLong(1);
                    System.out.println("User registered successfully with ID = " + generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            new SessionHandler(request, uid, username, email, role, interval);
            // Send response
            successResponse(response, username);
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
            e.printStackTrace();
        }
    }

    public void UserLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = request.getParameter("name");
            String password = request.getParameter("password");
            System.out.println("User Login: " + username + " " + password);

            st = con.prepareStatement(Queries.getUserByName);
            st.setString(1, username);
            r1 = st.executeQuery();
            String dbrole = "";

            if (r1.next()) {
                String dbPassword = r1.getString("password");
                dbrole = r1.getString("role");
                String email = r1.getString("email");
                String hashPassword = Hasher.hashPassword(username, password);
                if (dbPassword.equals(hashPassword) == false) {
                    errorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Invalid password.");
                    return;
                } else {
                    System.out.println("Login successful");
                    // Store user details in session
                    new SessionHandler(request, r1.getLong("u_id"), username, email, dbrole, interval);
                    // Send response
                    successResponse(response, dbrole);
                }
            } else {
                errorResponse(response, HttpServletResponse.SC_NOT_ACCEPTABLE, "Invalid username.");
            }
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_BAD_GATEWAY, e.getMessage());
            e.printStackTrace();
        }
    }

    public void UserLogout(HttpServletRequest request) {
        SessionHandler session = new SessionHandler(request);
        session.invalidateSession();
    }

    public void UserCheck(HttpServletRequest request, HttpServletResponse response) {
        SessionHandler session = new SessionHandler(request);
        String role = session.getRole();
        System.out.println("User Check: " + role);
        if (role == null || role == "")
            errorResponse(response, HttpServletResponse.SC_FORBIDDEN, "login");
        else
            successResponse(response, role);
    }
}
