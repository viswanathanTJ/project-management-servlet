package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.Authentication;
import activities.DBUtil.DatabaseConnection;
import activities.DBUtil.Query;
import activities.Queries;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/resetPassword")
public class ResetPassword extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int uid = Integer.parseInt(request.getParameter("uid"));
            String name = Query.getUserNameByID(uid);
            String password = request.getParameter("password");
            String hashPassword = Authentication.hashPassword(name, password);
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.updatePassword);
            st.setString(1, hashPassword);
            st.setInt(2, uid);
            st.executeUpdate();
            response.getWriter().print("Password updated successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            response.getWriter().print(e.getMessage());
            e.printStackTrace();
        }
    }

}
