package com.user.activities;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.user.activities.DBUtil.DatabaseConnection;
import models.UserModel;

/**
 * Servlet implementation class UserRegister
 */
@WebServlet("/Register")
public class UserRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserRegister() {
        super();
        // TODO Auto-generated constructor stub
    }

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
			String username = request.getParameter("name");
			String password = request.getParameter("password");
			String cpassword = request.getParameter("cpassword");
			System.out.println(password.equals(cpassword));
			if(password.equals(cpassword) == false) {
				 response.getWriter().print("Password Mismatch");
				 response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				 return;
			}
			System.out.println("Registration start");
			Connection con = DatabaseConnection.initializeDatabase();
			PreparedStatement st = con.prepareStatement("insert into user (name, email, password, role) values(?, ?, ?, ?)");
			st.setString(1, request.getParameter("name"));
			st.setString(2, request.getParameter("email"));
			String hashPassword = Authentication.hashPassword(request.getParameter("password"));
			st.setString(3, hashPassword);
			st.setString(4, "Employee");
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
