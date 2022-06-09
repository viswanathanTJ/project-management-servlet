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

import activities.DBUtil.DatabaseConnection;

@WebServlet("/deleteProject")
public class DeleteProject extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		System.out.println("Delete Project Request: " + id);
		try {
			Connection con;
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement st;
			st = con.prepareStatement("delete from projects where p_id=?");
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
