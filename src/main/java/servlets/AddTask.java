package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import models.Task;
import queries.Queries;
import service.DBUtil.DatabaseConnection;
import service.DBUtil.Query;
import service.SessionHandler;

@WebServlet("/AddTask")
public class AddTask extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String title = request.getParameter("title");
			String desc = request.getParameter("description");
			String startDate = request.getParameter("start_date");
			String endDate = request.getParameter("end_date");
			String priority = request.getParameter("priority");
			String assigneeID = request.getParameter("assignee");
			String assignee = Query.getUserNameByID(assigneeID);
			String pid = request.getParameter("project");
			SessionHandler session = new SessionHandler(request);
			System.out
					.println("Add Task: " + title + " " + desc + " " + startDate + " " + endDate + " " + priority + " "
							+ assignee + " " + pid);
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
			String creatorID = session.getUID();
			String cname = session.getName();

			st = con.prepareStatement(Queries.addTask, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, title);
			st.setString(2, desc);
			st.setString(3, startDate);
			st.setString(4, endDate);
			st.setString(5, priority);
			st.setString(6, assigneeID);
			st.setString(7, creatorID);
			st.setString(8, pid);
			st.setInt(9, 0);

			// Run Query
			int row = st.executeUpdate();
			if (row == 0)
				throw new SQLException("Creating user failed, no rows affected.");
			String tid;
			try (ResultSet generatedKeys = st.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					tid = generatedKeys.getString(1);
					System.out.println("Task created with ID = " + generatedKeys.getString(1));
				} else
					throw new SQLException("Creating task failed, no ID obtained.");
			}
			String project = Query.getProjectNameByID(pid);
			Task task = new Task(assignee, cname, "0", priority, project, startDate, tid, title);

			System.out.println(new Gson().toJson(task));
			response.getWriter().print(new Gson().toJson(task));
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
