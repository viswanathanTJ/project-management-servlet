package com.user.activities;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.user.activities.DBUtil.DatabaseConnection;
import models.UserModel;

/**
 * Servlet implementation class UserRegister
 */
@WebServlet("/register")
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
			Connection con = DatabaseConnection.initializeDatabase();
			PreparedStatement st = con.prepareStatement("insert into user (name, email, role) values(?, ?, ?)");
			st.setString(1, request.getParameter("name"));
			st.setString(2, request.getParameter("email"));
			st.setString(3, "Employee");
			st.executeUpdate();
			st.close();
            con.close();
			response.getWriter().print("User inserted successfully");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		System.out.println(request.getParameter("name"));
	}

}
