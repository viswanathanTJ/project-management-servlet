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

import com.google.gson.Gson;

import activities.DBUtil.DatabaseConnection;
import entities.Task;
import activities.SessionHandler;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/AddTask")
public class AddTask extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String title = request.getParameter("title");
			String desc = request.getParameter("description");
			String startDate = request.getParameter("start_date");
			String endDate = request.getParameter("end_date");
			int priority = Integer.parseInt(request.getParameter("priority"));
			String assignee = request.getParameter("assignee");
			String pid = request.getParameter("project");
			System.out
					.println("Add Task: " + title + " " + desc + " " + startDate + " " + endDate + " " + priority + " "
							+ assignee + " " + pid);
			if (startDate.equals(endDate)) {
				response.getWriter().print("Start and end date cannot be the same.");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement("SELECT title from tasks");
			ResultSet r1 = st.executeQuery();
			while (r1.next()) {
				if (r1.getString("title").equals(title)) {
					response.getWriter().print("Task title already exists.");
					response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
					return;
				}
			}
			SessionHandler session = new SessionHandler(request);

			String creatorID = session.getUID();
			st = con.prepareStatement(
					"INSERT INTO tasks (title, description, start_date, end_date, priority, assignee_id, creator_id, p_id, completed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			st.setString(1, title);
			st.setString(2, desc);
			st.setString(3, startDate);
			st.setString(4, endDate);
			st.setInt(5, priority);
			st.setString(6, assignee);
			st.setString(7, creatorID);
			st.setString(8, pid);
			st.setInt(9, 0);
			st.executeUpdate();
			Task task = new Task(title, desc, startDate, endDate, priority, assignee, creatorID, pid);
			response.getWriter().print(new Gson().toJson(task));
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			st.close();
			con.close();
		} catch (Exception e) {
			// } catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
