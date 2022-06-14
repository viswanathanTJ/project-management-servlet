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

import org.json.JSONArray;
import org.json.JSONObject;

import activities.Queries;
import activities.DBUtil.DatabaseConnection;
import activities.DBUtil.Query;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/getUsersByPID")
public class GetUsersByPID extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		try {
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement(Queries.getUsersByPID);
			st.setString(1, pid);
			ResultSet r1 = st.executeQuery();
			JSONObject jsonObject = new JSONObject();
			JSONArray array = new JSONArray();
			while (r1.next()) {
				JSONObject obj = new JSONObject();
				obj.put("uid", r1.getString("u_id"));
				obj.put("name", Query.getUserNameByID(r1.getInt("u_id")));
				array.put(obj);
			}
			jsonObject.put("users", array);
			response.setContentType("application/json");
			response.getWriter().print(jsonObject);
			response.setStatus(200);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}

}