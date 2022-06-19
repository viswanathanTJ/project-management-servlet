package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import queries.Queries;
import service.DBUtil.DatabaseConnection;
import service.DBUtil.Query;

@WebServlet("/ModifyMembers")
public class ModifyMembers extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		try {
			HashSet<Integer> set = new HashSet<Integer>();
			String pid = request.getParameter("pid");
			String name = request.getParameter("ename");
			String desc = request.getParameter("edesc");
			String oname = request.getParameter("eownerList");
			System.out.println("Add Members:");
			JSONObject resp = new JSONObject();
			Connection con;
			con = DatabaseConnection.getDatabase();
			String oid = Query.getUserIDByName(oname);
			System.out
					.println("pid: " + pid + " oid: " + oid + " oname: " + oname + " name: " + name + " desc: " + desc);
			if (oid == null || oid == "") {
				resp.put("error", "No such user");
				out.write(resp.toString());
				return;
			}
			System.out.println("Add Members:- pid: " + pid + " oname: " + oname);
			Enumeration<String> parameterNames = request.getParameterNames();

			PreparedStatement st;
			ResultSet r1;
			st = con.prepareStatement(Queries.updateProject);
			st.setString(1, name);
			st.setString(2, desc);
			st.setString(3, oid);
			st.setString(4, pid);
			st.executeUpdate();
			System.out.println("Project updated");
			int affected = 0;
			int count = 0;
			while (parameterNames.hasMoreElements()) {
				String paramName = parameterNames.nextElement();
				if (paramName.equals("pid") || paramName.equals("eownerList") ||
						paramName.equals("ename") ||
						paramName.equals("edesc"))
					continue;
				System.out.println("paramName: " + paramName);
				String uid = request.getParameter(paramName);
				st = con.prepareStatement(Queries.addMembers);
				set.add(Integer.parseInt(uid));
				System.out.println(uid);
				st.setString(1, pid);
				st.setString(2, uid);
				st.setString(3, oid);
				st.setString(4, pid);
				st.setString(5, uid);
				st.executeUpdate();
				affected++;
				count++;
			}
			resp.put("count", count);
			// out.write(affected + " rows inserted" + "\n");
			System.out.println(affected + " rows affected");
			st = con.prepareStatement(Queries.uidInProUsers);
			st.setString(1, pid);
			r1 = st.executeQuery();
			affected = 0;
			while (r1.next()) {
				if (!set.contains(r1.getInt("u_id"))) {
					affected++;
					st = con.prepareStatement(Queries.deleteProUser);
					st.setString(1, r1.getString("sno"));
					st.executeUpdate();
				}
			}
			// out.write(affected + " rows removed");
			resp.put("removed", affected);
			out.print(resp);
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			System.out.println(affected + " rows removed");
		} catch (SQLException e) {
			e.printStackTrace();
			out.write(e.getMessage());
		}
	}

}
