package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.Queries;
import activities.DBUtil.DatabaseConnection;
import activities.DBUtil.Query;

@WebServlet("/updateProject")
public class UpdateProject extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String projectName = request.getParameter("ename");
			String edesc = request.getParameter("edesc");
			String eowner = request.getParameter("eowner");
			String id = request.getParameter("id");
			System.out.println("Update Project Request: " + projectName + " " + edesc + " " + eowner + " " + id);
			// Update project
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st;
			st = con.prepareStatement(Queries.getProjectByID);
			st.setString(1, id);
			ResultSet r1 = st.executeQuery();

			if (r1.next()) {
				String oldProjectname = r1.getString("name");
				if (!oldProjectname.equals(projectName)) {
					st = con.prepareStatement(Queries.getProjectByName);
					st.setString(1, projectName);
					ResultSet r2 = st.executeQuery();
					if (r2.next()) {
						response.getWriter().print("Project name already exists.");
						response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
						return;
					}
				}
			}

			st = con.prepareStatement("update projects set name=?, description=?, owner_id=? where p_id=?");
			st.setString(1, projectName);
			st.setString(2, edesc);
			String ownerId = Query.getUserIDByName(eowner);
			if (ownerId == null) {
				response.getWriter().print("Username not found.");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			st.setString(3, ownerId);
			st.setString(4, id);
			System.out.println(st.toString());
			st.executeUpdate();

			response.getWriter().print("Project updated successfully.");
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			response.getWriter().println("Error while updating project.");
			response.getWriter().print(e.getMessage());
			System.out.println(e.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
