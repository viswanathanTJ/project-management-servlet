package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import activities.DBUtil.DatabaseConnection;
import entities.Project;
import java.text.SimpleDateFormat;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/AddProject")
public class AddProject extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String name = request.getParameter("name");
			String desc = request.getParameter("desc");
			String owner = request.getParameter("ownerList");
			System.out.println("Add Project: " + name + " " + desc + " " + owner);
			HttpSession session = request.getSession();
			String uid = (String) session.getAttribute("uid");
			String ownerName = (String) session.getAttribute("name");
			System.out.println("Session: " + uid + " " + ownerName);
			PreparedStatement st;
			Connection con;
			ResultSet r1;
			con = DatabaseConnection.getDatabase();

			st = con.prepareStatement("select u_id from user where name=?");
			st.setString(1, owner);
			r1 = st.executeQuery();
			if (r1.next())
				uid = r1.getString("u_id");

			st = con.prepareStatement("select * from projects where name=?");
			st.setString(1, name);
			r1 = st.executeQuery();
			if (r1.next()) {
				response.getWriter().print("Project name already exists.");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			st = con.prepareStatement("insert into projects (name, description, owner_id) values(?, ?, ?)");
			st.setString(1, name);
			st.setString(2, desc);
			st.setString(3, uid);
			st.executeUpdate();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(new Date());
			Project project = new Project(name, desc, uid, owner, currentTime);
			response.getWriter().print(new Gson().toJson(project));
			response.setStatus(HttpServletResponse.SC_OK);
			st.close();
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print(e.getMessage());
		}
	}

}
