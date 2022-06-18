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

import activities.Hasher;
import activities.Queries;
import activities.ResponseHandler;
import activities.SessionHandler;
import activities.DBUtil.DatabaseConnection;

/**
 * Servlet implementation class UserLogin
 */
@WebServlet("/Login")
public class UserLogin extends HttpServlet {

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
				String hashPassword = Hasher.hashPassword(username, password);
				if (dbPassword.equals(hashPassword) == false) {
					ResponseHandler.errorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Invalid password.");
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
				ResponseHandler.errorResponse(response, HttpServletResponse.SC_NOT_ACCEPTABLE, "Invalid username.");
				return;
			}
			st.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			ResponseHandler.errorResponse(response, HttpServletResponse.SC_BAD_GATEWAY, e.getMessage());
			e.printStackTrace();
		}
	}
}
