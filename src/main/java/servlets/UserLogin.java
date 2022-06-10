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

import activities.Authentication;
import activities.Queries;
import activities.SessionHandler;
import activities.DBUtil.DatabaseConnection;

/**
 * Servlet implementation class UserLogin
 */
@WebServlet("/Login")
public class UserLogin extends HttpServlet {
	private void errorResponse(HttpServletResponse res, String message) throws IOException {
		res.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		res.getWriter().print(message);
		return;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			System.out.println("Login1");
			String username = request.getParameter("name");
			String password = request.getParameter("password");
			System.out.println(username + " " + password);

			Connection con;
			con = DatabaseConnection.getDatabase();

			PreparedStatement st = con.prepareStatement(Queries.getUserByName);
			st.setString(1, username);
			ResultSet r1 = st.executeQuery();
			String dbrole = "";
			if (r1.next()) {
				String dbPassword = r1.getString("password");
				dbrole = r1.getString("role");
				String email = r1.getString("email");
				String hashPassword = Authentication.hashPassword(username, password);
				if (dbPassword.equals(hashPassword) == false) {
					errorResponse(response, "Invalid password.");
					return;
				} else {
					System.out.println("Login successful");
					SessionHandler session = new SessionHandler(request);
					session.setEmail(email);
					session.setName(username);
					session.setRole(dbrole);
					session.setUID(r1.getString("u_id"));
					session.setInterval(-1);
					response.getWriter().print(dbrole);
				}
			} else {
				response.getWriter().print("Invalid Username.");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			st.close();
			con.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			response.getWriter().print("Connection error123.");
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} catch (SQLException e) {
			response.getWriter().print("SQL Exception error.");
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
