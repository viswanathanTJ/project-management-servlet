package com.user.activities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.user.activities.DBUtil.DatabaseConnection;

/**
 * Servlet implementation class UserLogin
 */
@WebServlet("/Login")
public class UserLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			TimeUnit.SECONDS.sleep(3);
			Connection con = DatabaseConnection.initializeDatabase();
			String email = request.getParameter("email");
			String username = request.getParameter("name");
			System.out.println("Loggin in "+email);
			PreparedStatement st = con.prepareStatement("select * from user where name=?");
			st.setString(1, username);
			ResultSet r1=st.executeQuery();
			String dbrole = "";
			if(r1.next()) {
				String dbPassword = r1.getString("password");
				dbrole = r1.getString("role");
				String hashPassword = Authentication.hashPassword(request.getParameter("password"));
				if(dbPassword.equals(hashPassword) == false) {
					response.getWriter().print("Invalid Password.");
					response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
					return;
				} else {
					JSONObject user = new JSONObject();
					user.put("name", username);
					user.put("role", dbrole);
					response.getWriter().print(user);
				}
			}
			else {
				response.getWriter().print("Invalid Username.");
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				return;
			}
			
//			st.close();
//            con.close();
		} catch (ClassNotFoundException | SQLException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
