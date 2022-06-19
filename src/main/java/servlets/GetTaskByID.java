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

import queries.Queries;
import service.DBUtil.DatabaseConnection;
import service.DBUtil.Query;

@WebServlet("/getTaskByID")
public class GetTaskByID extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String tid = request.getParameter("tid");
		try {
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement(Queries.getTaskByID);
			st.setString(1, tid);
			ResultSet r1 = st.executeQuery();
			JSONObject obj = new JSONObject();
			if (r1.next()) {
				obj.put("t_id", r1.getInt("t_id"));
				obj.put("title", r1.getString("title"));
				obj.put("desc", r1.getString("description"));
				obj.put("start_date", r1.getString("start_date").substring(0, 10));
				obj.put("due_date", r1.getString("end_date").substring(0, 10));
				obj.put("priority", r1.getString("priority"));
				obj.put("assignee_id", r1.getInt("assignee_id"));
				obj.put("completed", r1.getInt("completed"));
				obj.put("assignee", Query.getUserNameByID(r1.getInt("assignee_id")));
				obj.put("p_id", r1.getString("p_id"));
				obj.put("project_name", Query.getProjectNameByID(r1.getString("p_id")));
				obj.put("c_id", r1.getInt("creator_id"));
				obj.put("cname", Query.getUserNameByID(r1.getInt("creator_id")));
				obj.put("desc", r1.getString("description"));
				Timestamp t = r1.getTimestamp("createdat");
				SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
				String time = df.format(t);
				obj.put("createdat", time);
			}
			response.setContentType("application/json");
			response.getWriter().print(obj);
			response.setStatus(200);
		} catch (SQLException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}

}
