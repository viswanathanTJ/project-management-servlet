package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.Queries;
import activities.DBUtil.DatabaseConnection;

@WebServlet("/updateUser")
public class UpdateUser extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String username = request.getParameter("ename");
			String email = request.getParameter("eemail");
			// String password = request.getParameter("epassword");
			String role = request.getParameter("erole");
			String id = request.getParameter("id");
			System.out.println("Update Request: " + username + " " + email + " " + role + " " + id);
			Connection con;
			con = DatabaseConnection.getDatabase();

			PreparedStatement st;
			st = con.prepareStatement(Queries.getUserNameEmailByID);
			st.setString(1, id);
			ResultSet r1 = st.executeQuery();
			if (r1.next()) {
				String oldUsername = r1.getString("name");
				String oldEmail = r1.getString("email");
				if (!oldUsername.equals(username)) {
					st = con.prepareStatement(Queries.getUserByName);
					st.setString(1, username);
					ResultSet r2 = st.executeQuery();
					if (r2.next()) {
						response.getWriter().print("Username already exists.");
						response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
						return;
					}
				}
				if (!oldEmail.equals(email)) {
					st = con.prepareStatement(Queries.getUserByEmail);
					st.setString(1, email);
					ResultSet r2 = st.executeQuery();
					if (r2.next()) {
						response.getWriter().print("Email ID already exists.");
						response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
						return;
					}
				}
			}
			st = con.prepareStatement(Queries.updateUserByID);
			st.setString(1, username);
			st.setString(2, email);
			// st.setString(3, password);
			st.setString(3, role);
			st.setInt(4, Integer.parseInt(id));
			st.executeUpdate();
			response.getWriter().print("User updated successfully.");
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			response.getWriter().println("Error while updating user.");
			response.getWriter().print(e.getMessage());
			System.out.println(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
