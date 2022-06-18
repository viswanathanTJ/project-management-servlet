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

import org.json.JSONObject;

import activities.DBUtil.DatabaseConnection;
import activities.Queries;

@WebServlet("/getAllCounts")
public class GetAllCounts extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement(Queries.getAllCounts);
			ResultSet r1 = st.executeQuery();
			JSONObject obj = new JSONObject();
			if (r1.next()) {
				obj.put("projects", r1.getInt("projects"));
				obj.put("users", r1.getInt("users"));
				obj.put("tasks", r1.getInt("tasks"));
				obj.put("open", r1.getInt("open"));
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
