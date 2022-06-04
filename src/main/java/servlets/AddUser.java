package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import activities.Authentication;
import activities.DBUtil.DatabaseConnection;
import entities.User;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/AddUser")
public class AddUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.print("Done");
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String username = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String role = request.getParameter("role");
			response.getWriter().print(username+" "+email+" "+password+" "+role);
			 Connection con;
			 	con = DatabaseConnection.initializeDatabase();
			
			 PreparedStatement st = con.prepareStatement("select * from user where email=?");
			st.setString(1,email);
			ResultSet r1=st.executeQuery();
			if(r1.next()) {
				response.getWriter().print("Mail ID already exists.");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			st = con.prepareStatement("select * from user where name=?");
			st.setString(1,username);
			r1=st.executeQuery();
			if(r1.next()) {
				response.getWriter().print("Username already exists.");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			st = con.prepareStatement("insert into user (name, email, password, role) values(?, ?, ?, ?)");
			st.setString(1, username);
			st.setString(2, email);
			String hashPassword = Authentication.hashPassword(username, password);
			st.setString(3, hashPassword);
			st.setString(4, role);
			st.executeUpdate();
			st.close();
			con.close();
			response.getWriter().print("Added successfully");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

}
