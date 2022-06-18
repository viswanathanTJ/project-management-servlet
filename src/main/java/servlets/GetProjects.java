package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import activities.DBUtil.DatabaseConnection;
import activities.DBUtil.Query;
import activities.Queries;
import models.Project;

@WebServlet("/getProjects")
public class GetProjects extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		try {
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement(Queries.getProjectsDetails);
			ResultSet r1 = st.executeQuery();
			List<Project> projects = new ArrayList<Project>();
			while (r1.next()) {
				Project project = new Project();
				project.setP_id(r1.getString("p_id"));
				project.setTeam(r1.getString("team"));
				project.setTasks(r1.getString("tasks"));
				int oid = r1.getInt("owner_id");
				if (oid == 0)
					project.setOname("unassigned");
				else
					project.setOname(Query.getUserNameByID(oid));
				project.setName(r1.getString("name"));
				project.setDesc(r1.getString("description"));
				project.setPriority(r1.getString("priority"));
				Timestamp t = r1.getTimestamp("createdat");
				SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
				String time = df.format(t);
				project.setCreatedat(time);
				projects.add(project);
			}
			jsonObject.put("projects", projects);
			response.setContentType("application/json");
			response.getWriter().print(jsonObject);
			response.setStatus(200);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}

}
