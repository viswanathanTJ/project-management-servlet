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

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/getProjectMembers")
public class GetProjectMembers extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		System.out.println("GetProjectMembers " + pid);
		JSONObject jsonObject = new JSONObject();
		JSONArray array = new JSONArray();
		try {
			Connection con;
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement st = con.prepareStatement("select u_id from project_users p1 where p1.p_id = ?");
			st.setString(1, pid);
			ResultSet r1 = st.executeQuery();
			while (r1.next()) {
				JSONObject obj = new JSONObject();
				int uid = r1.getInt("u_id");
				System.out.println("UID:" + uid);
				obj.put("uid", uid);
				obj.put("name", Query.getUserNameByID(uid));
				array.put(obj);
			}
			jsonObject.put("members", array);
			response.getWriter().print(jsonObject);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}

}
