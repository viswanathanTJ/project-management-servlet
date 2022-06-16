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

import org.json.JSONArray;
import org.json.JSONObject;

import activities.DBUtil.DatabaseConnection;
import activities.Queries;

@WebServlet("/getRecentTasks")
public class GetRecentTasks extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			System.out.println("GetRecentTasks");
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement(Queries.getRecentTasks);
			ResultSet r1 = st.executeQuery();
			JSONObject jsonObject = new JSONObject();
			JSONArray array = new JSONArray();
			while (r1.next()) {
				JSONObject obj = new JSONObject();
				obj.put("title", r1.getString("title"));
				obj.put("priority", r1.getString("priority"));
				obj.put("owner", r1.getString("owner"));
				obj.put("assignee", r1.getString("assignee"));
				obj.put("project", r1.getString("project"));
				array.put(obj);
			}
			jsonObject.put("tasks", array);
			response.setContentType("application/json");
			response.getWriter().print(jsonObject);
			response.setStatus(200);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}

}
