package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import activities.Authentication;
import activities.DBUtil;
import activities.DBUtil.DatabaseConnection;
import activities.DBUtil.Query;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/getProjects")
public class GetProjects extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jsonObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Connection con;
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement st = con.prepareStatement("select * from projects");
			ResultSet r1 = st.executeQuery();
			while (r1.next()) {
				JSONObject obj = new JSONObject();
				obj.put("p_id", r1.getInt("p_id"));
				obj.put("owner_id", r1.getInt("owner_id"));
				obj.put("oname", Query.getUserNameByID(r1.getInt("owner_id")));
				obj.put("name", r1.getString("name"));
				obj.put("desc", r1.getString("description"));
				Timestamp t = r1.getTimestamp("createdat");
				SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
				String time = df.format(t);
				obj.put("createdat", time);
				array.put(obj);
			}
			jsonObject.put("projects", array);
			response.getWriter().print(jsonObject);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}

}