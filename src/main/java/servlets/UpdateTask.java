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

@WebServlet("/updateTask")
public class UpdateTask extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String title = request.getParameter("title");
			String desc = request.getParameter("description");
			String startDate = request.getParameter("start_date");
			String endDate = request.getParameter("due_date");
			int priority = Integer.parseInt(request.getParameter("priority"));
			int completed = Integer.parseInt(request.getParameter("completed"));
			String assignee = request.getParameter("assignee");
			System.out.println(
					"Update Task: " + title + " " + desc + " " + startDate + " " + endDate + " " + priority + " "
							+ assignee + " " + completed);
			if (startDate.equals(endDate)) {
				response.getWriter().print("Start and end date cannot be the same.");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement("SELECT title from tasks");
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
