package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.Queries;
import activities.DBUtil.DatabaseConnection;

@WebServlet("/deleteUser")
public class DeleteUser extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		System.out.println("Delete Request: " + id);
		if (id.equals("1")) {
			response.getWriter().print("Unable to delete admin");
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		try {
			Connection con;
			con = DatabaseConnection.getDatabase();

			PreparedStatement st;
			st = con.prepareStatement(Queries.deleteUser);
			st.setString(1, id);
			st.executeUpdate();
			response.getWriter().print("Deleted successfully");
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}

}
