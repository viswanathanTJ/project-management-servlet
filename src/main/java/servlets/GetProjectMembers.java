package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import activities.Queries;
import activities.DBUtil.DatabaseConnection;

@WebServlet("/getProjectMembers")
public class GetProjectMembers extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pid = request.getParameter("pid");
		System.out.println("GetProjectMembers " + pid);
		JSONObject jsonObject = new JSONObject();
		JSONArray array = new JSONArray();
		HashSet<Integer> set = new HashSet<Integer>();
		boolean hasMembers = false;
		try {
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement(Queries.getProjectMembers);
			st.setString(1, pid);
			ResultSet r1 = st.executeQuery();
			int oid = 1;
			while (r1.next()) {
				oid = r1.getInt("o_id");
				set.add(r1.getInt("u_id"));
			}
			st = con.prepareStatement(Queries.getUserIDName);
			r1 = st.executeQuery();
			while (r1.next()) {
				int uid = r1.getInt("u_id");
				if (uid == oid)
					continue;
				JSONObject obj = new JSONObject();
				obj.put("uid", uid);
				obj.put("name", r1.getString("name"));
				if (set.contains(uid)) {
					obj.put("isMember", "checked");
					hasMembers = true;
				} else
					obj.put("isMember", "");
				array.put(obj);
			}
			jsonObject.put("members", array);
			jsonObject.put("hasMembers", hasMembers);
			response.getWriter().print(jsonObject);
			response.setStatus(200);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().print("Error: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
