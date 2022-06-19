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

import queries.Queries;
import service.DBUtil.DatabaseConnection;

@WebServlet("/deleteTask")
public class DeleteTask extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		System.out.println("Delete Task Request: " + id);
		try {
			Connection con;
			con = DatabaseConnection.getDatabase();

			PreparedStatement st;
			st = con.prepareStatement(Queries.deleteTask);
			st.setString(1, id);
			st.executeUpdate();
			response.getWriter().print("Deleted successfully");
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (SQLException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}

}
