package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import activities.Hasher;
import activities.Queries;
import activities.DBUtil.DatabaseConnection;
import models.User;

@WebServlet("/AddUser")
public class AddUser extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String username = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String role = request.getParameter("role");
			System.out.println("Add User: " + username + " " + email + " " + password + " " + role);
			Connection con;
			con = DatabaseConnection.getDatabase();

			PreparedStatement st = con.prepareStatement(Queries.getUserByEmail);
			st.setString(1, email);
			ResultSet r1 = st.executeQuery();
			if (r1.next()) {
				response.getWriter().print("Mail ID already exists.");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			st = con.prepareStatement(Queries.getUserByName);
			st.setString(1, username);
			r1 = st.executeQuery();
			if (r1.next()) {
				response.getWriter().print("Username already exists.");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			st = con.prepareStatement("insert into user (name, email, password, role) values(?, ?, ?, ?)");
			st.setString(1, username);
			st.setString(2, email);
			String hashPassword = Hasher.hashPassword(username, password);
			st.setString(3, hashPassword);
			st.setString(4, role);
			st.executeUpdate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(new Date());
			User user = new User(username, email, role, currentTime);
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().print(new Gson().toJson(user));
			st.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
