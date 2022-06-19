package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.DBUtil.DatabaseConnection;
import activities.Queries;
import activities.SessionHandler;

@WebServlet("/AddProject")
public class AddProject extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String name = request.getParameter("name");
			String desc = request.getParameter("desc");
			String priority = request.getParameter("priority");
			System.out.println("Add Project: " + name + " " + desc + " " + priority);
			String uid;
			SessionHandler session = new SessionHandler(request);

			String oid = session.getUID();
			String owner = session.getName();
			System.out.println("Owner ID " + oid + " Owner Name " + owner);
			PreparedStatement st;
			Connection con;
			ResultSet r1;
			con = DatabaseConnection.getDatabase();

			st = con.prepareStatement(Queries.getProjectByName);
			st.setString(1, name);
			r1 = st.executeQuery();
			if (r1.next()) {
				response.getWriter().print("Project name already exists.");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			st = con.prepareStatement(Queries.putIntoProject, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, name);
			st.setString(2, desc);
			st.setString(3, priority);
			st.setString(4, oid);
			st.executeUpdate();
			System.out.println("Project added");
			int pid;
			ResultSet rs = st.getGeneratedKeys();
			if (rs.next())
				pid = rs.getInt(1);
			else
				pid = -1;
			System.out.println("Project ID generated is: " + pid);
			// Add Members
			Enumeration<String> parameterNames = request.getParameterNames();
			HashSet<Integer> set = new HashSet<Integer>();

			int affected = 0;
			while (parameterNames.hasMoreElements()) {
				String paramName = parameterNames.nextElement();
				if (paramName.equals("name") || paramName.equals("desc") || paramName.equals("priority"))
					continue;
				System.out.println("User ID: " + paramName);
				uid = request.getParameter(paramName);
				st = con.prepareStatement(Queries.addMembers);
				set.add(Integer.parseInt(uid));
				System.out.println(uid);
				st.setInt(1, pid);
				st.setString(2, uid);
				st.setString(3, oid);
				st.setInt(4, pid);
				st.setString(5, uid);
				affected = st.executeUpdate();
			}
			System.out.println("Added: " + affected + " employees to project " + pid);
			st.close();
			con.close();
			response.getWriter().print(pid);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print(e.getMessage());
		}
	}

}
