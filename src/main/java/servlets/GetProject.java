package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import models.Project;
import queries.Queries;
import service.DBUtil.DatabaseConnection;
import service.DBUtil.Query;

@WebServlet("/getProject")
public class GetProject extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		try {
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement(Queries.getProjectByIDDetails);
			st.setString(1, pid);
			ResultSet r1 = st.executeQuery();
			Project project = new Project();
			if (r1.next()) {
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
			}
			response.setContentType("application/json");
			response.getWriter().print(new Gson().toJson(project));
			response.setStatus(200);
		} catch (SQLException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}

}
