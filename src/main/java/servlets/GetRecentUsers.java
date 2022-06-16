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

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/getRecentUsers")
public class GetRecentUsers extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			System.out.println("GetRecentUsers");
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement(Queries.getRecentUsers);
			ResultSet r1 = st.executeQuery();
			JSONObject jsonObject = new JSONObject();
			JSONArray array = new JSONArray();
			while (r1.next()) {
				JSONObject obj = new JSONObject();
				obj.put("name", r1.getString("name"));
				obj.put("role", r1.getString("role"));
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