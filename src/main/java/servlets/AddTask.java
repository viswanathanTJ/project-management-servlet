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

import models.Task_Add;
import service.SessionHandler;
import service.DBUtil.DatabaseConnection;
import service.DBUtil.Query;

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
			int priority = Integer.parseInt(request.getParameter("priority"));
			String assigneeID = request.getParameter("assignee");
			String assignee = Query.getUserNameByID(Integer.parseInt(assigneeID));
			String pid = request.getParameter("project");
			SessionHandler session = new SessionHandler(request);

			String creator = session.getName();
			System.out
					.println("Add Task: " + title + " " + desc + " " + startDate + " " + endDate + " " + priority + " "
							+ assigneeID + " " + assignee + " " + pid);
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
			int creatorID = Integer.parseInt(session.getUID());
			String cname = session.getName();

			st = con.prepareStatement(
					"INSERT INTO tasks (title, description, start_date, end_date, priority, assignee_id, creator_id, p_id, completed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			st.setString(1, title);
			st.setString(2, desc);
			st.setString(3, startDate);
			st.setString(4, endDate);
			st.setInt(5, priority);
			st.setString(6, assigneeID);
			st.setInt(7, creatorID);
			st.setString(8, pid);
			st.setInt(9, 0);
			st.executeUpdate();
			String project = Query.getProjectNameByID(pid);
			Task_Add task = new Task_Add(creator, title, desc, startDate, endDate, priority,
					assigneeID, assignee, creatorID, cname, pid, project, 0);

			System.out.println(new Gson().toJson(task));
			response.getWriter().print(new Gson().toJson(task));
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);

			con.close();
		} catch (Exception e) {
			// } catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
