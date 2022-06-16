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

import org.json.JSONObject;

import activities.DBUtil.DatabaseConnection;
import activities.DBUtil.Query;
import activities.Queries;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/getProject")
public class GetProject extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int pid = Integer.parseInt(request.getParameter("pid"));
		try {
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement(Queries.getProjectByID);
			st.setInt(1, pid);
			PreparedStatement st2;
			ResultSet r1 = st.executeQuery();
			ResultSet r2;
			JSONObject obj = new JSONObject();
			if (r1.next()) {
				obj.put("p_id", pid);
				st2 = con.prepareStatement(Queries.getUserCountOnProject);
				st2.setInt(1, pid);
				r2 = st2.executeQuery();
				if (r2.next())
					obj.put("team", r2.getInt("team"));
				st2 = con.prepareStatement(Queries.getTaskCount);
				st2.setInt(1, pid);
				r2 = st2.executeQuery();
				if (r2.next())
					obj.put("tasks", r2.getInt("tasks"));
				obj.put("owner_id", r1.getInt("owner_id"));
				obj.put("oname", Query.getUserNameByID(r1.getInt("owner_id")));
				obj.put("name", r1.getString("name"));
				obj.put("desc", r1.getString("description"));
				obj.put("priority", r1.getInt("priority"));
				Timestamp t = r1.getTimestamp("createdat");
				SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
				String time = df.format(t);
				obj.put("createdat", time);
			}
			response.setContentType("application/json");
			response.getWriter().print(obj);
			response.setStatus(200);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}

}
