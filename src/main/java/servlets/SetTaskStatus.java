package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import activities.Queries;
import activities.DBUtil.DatabaseConnection;
import activities.DBUtil.Query;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/setTaskStatus")
public class SetTaskStatus extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String tid = request.getParameter("tid");
		String status = request.getParameter("status");
		try {
			Connection con;
			con = DatabaseConnection.getDatabase();
			PreparedStatement st = con.prepareStatement(Queries.setTaskStatus);
			st.setString(1, status);
			st.setString(2, tid);
			st.executeUpdate();
			response.setStatus(200);
		} catch (ClassNotFoundException | SQLException e) {
			response.getWriter().print(e.getMessage());
			e.printStackTrace();
		}
	}

}
