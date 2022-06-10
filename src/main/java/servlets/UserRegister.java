package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import activities.Authentication;
import activities.Queries;
import activities.DBUtil.DatabaseConnection;

@WebServlet("/register")
public class UserRegister extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Connection con;
			con = DatabaseConnection.getDatabase();

			String username = request.getParameter("name");
			String email = request.getParameter("email");
			PreparedStatement st = con.prepareStatement(Queries.getUserByEmail);
			st.setString(1, email);
			ResultSet r1 = st.executeQuery();
			if (r1.next()) {
				ResponseHandler.errorResponse(response, 406, "Mail ID already exists.");
				return;
			}
			st = con.prepareStatement(Queries.getUserByName);
			st.setString(1, username);
			r1 = st.executeQuery();
			if (r1.next()) {
				ResponseHandler.errorResponse(response, 406, "Username already exists.");
				return;
			}
			String password = request.getParameter("password");
			String cpassword = request.getParameter("cpassword");

			if (password.equals(cpassword) == false) {
				ResponseHandler.errorResponse(response, 406, "Passwords do not match.");
				return;
			}

			String role = "employee";

			st = con.prepareStatement("insert into user (name, email, password, role) values(?, ?, ?, ?)");
			st.setString(1, request.getParameter("name"));
			st.setString(2, request.getParameter("email"));
			String hashPassword = Authentication.hashPassword(username, password);
			st.setString(3, hashPassword);
			st.setString(4, role);
			st.executeUpdate();
			HttpSession session = request.getSession();
			session.setAttribute("email", email);
			session.setAttribute("name", username);
			session.setAttribute("role", role);
			session.setMaxInactiveInterval(10 * 60);
			JSONObject user = new JSONObject();
			user.put("name", ResponseHandler.encrypt(username));
			user.put("role", ResponseHandler.encrypt(role));
			ResponseHandler.successResponse(response, user.toString());
			st.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			ResponseHandler.errorResponse(response, 406, e.getMessage());
			e.printStackTrace();
		}
	}
}
