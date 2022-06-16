package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import activities.DBUtil.DatabaseConnection;
import activities.DBUtil.Query;
import activities.Queries;
import activities.SessionHandler;

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
			int assignee = Integer.parseInt(request.getParameter("assignee"));
			String tid = request.getParameter("t_id");
			System.out.println(
					"Update Task: " + title + " " + desc + " " + startDate + " " + endDate + " " + priority + " "
							+ assignee + " " + completed + " " + tid);

			Connection con;
			con = DatabaseConnection.getDatabase();
			SessionHandler session = new SessionHandler(request);
			String creator = session.getName();
			// Task update

			PreparedStatement st = con.prepareStatement(Queries.updateTask);
			st.setString(1, title);
			st.setString(2, desc);
			st.setString(3, startDate);
			st.setString(4, endDate);
			st.setInt(5, priority);
			st.setInt(6, assignee);
			st.setInt(7, completed);
			st.setString(8, tid);
			st.executeUpdate();
			System.out.println("Task updated successfully.");
			JSONObject obj = new JSONObject();
			obj.put("title", title);
			obj.put("cname", creator);
			obj.put("start_date", startDate);
			obj.put("due_date", endDate);
			obj.put("assignee", Query.getUserNameByID(assignee));
			obj.put("priority", priority);
			obj.put("completed", completed);
			System.out.println(obj);
			response.getWriter().print(obj);
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			response.getWriter().println("Error while updating user.");
			response.getWriter().print(e.getMessage());
			System.out.println(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
