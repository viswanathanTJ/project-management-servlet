package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import activities.Authentication;
import activities.DBUtil.DatabaseConnection;
import activities.DBUtil.Query;
import entities.User;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/AddMembers")
public class AddMembers extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String pid = request.getParameter("pid");
			String oname = request.getParameter("oname");
			String oid = Query.getUserIDByName(oname);
			PrintWriter out = response.getWriter();
			System.out.println("Add Members:- pid: " + pid + " oname: " + oname);
			out.write(pid + " " + oname + "\n");
			Enumeration<String> parameterNames = request.getParameterNames();
			Connection con;
			con = DatabaseConnection.initializeDatabase();
			PreparedStatement st;
			while (parameterNames.hasMoreElements()) {
				String paramName = parameterNames.nextElement();
				st = con.prepareStatement("insert into project_users (p_id, u_id, o_id) values(?, ?, ?)");
				st.setString(1, pid);
				st.setString(2, request.getParameter(paramName));
				st.setString(3, oid);
				st.executeUpdate();
				// out.write(paramName + "=" + request.getParameter(paramName) + "\n");
				System.out.print(paramName + "=" + request.getParameter(paramName) + "\n");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
