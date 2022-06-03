package activities;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import models.UserModel;

/**
 * Servlet implementation class UserRegister
 */
@WebServlet("/register")
public class UserRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		UserModel userModel = new UserModel();
		String action = request.getParameter("action");
		
		if(action.equalsIgnoreCase("find")) {
			out.print(userModel.find());
		} else if(action.equalsIgnoreCase("findAll"))
			out.print(userModel.findAll());
		out.flush();
		out.close();
	}
    


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Connection con = DatabaseConnection.initializeDatabase();
			String username = request.getParameter("name");
			String email = request.getParameter("email");
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
			String password = request.getParameter("password");
			String cpassword = request.getParameter("cpassword");
			System.out.println(password.equals(cpassword));
			if(password.equals(cpassword) == false) {
				 response.getWriter().print("Password Mismatch");
				 response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				 return;
			}
			System.out.println("Registration start");
			st = con.prepareStatement("insert into user (name, email, password, role) values(?, ?, ?, ?)");
			st.setString(1, request.getParameter("name"));
			st.setString(2, request.getParameter("email"));
			String hashPassword = Authentication.hashPassword(username, password);
			st.setString(3, hashPassword);
			st.setString(4, "employee");
			st.executeUpdate();
			st.close();
            con.close();
            JSONObject user = new JSONObject();
            user.put("name", username);
            user.put("role", "employee");
			response.getWriter().print(user);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		System.out.println(request.getParameter("name"));
	}

}
