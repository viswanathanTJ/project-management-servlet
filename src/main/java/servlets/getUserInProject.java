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
import activities.DBUtil.Query;
import activities.Queries;

@WebServlet("/getUsersInProject")
public class getUserInProject extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		JSONObject jsonObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement(Queries.getUsersByPID);
			st.setString(1, pid);
			ResultSet r1 = st.executeQuery();
			while (r1.next()) {
				JSONObject obj = new JSONObject();
				obj.put("uid", r1.getInt("u_id"));
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
