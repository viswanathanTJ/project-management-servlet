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

import queries.Queries;
import service.Hasher;
import service.DBUtil.DatabaseConnection;
import service.DBUtil.Query;

@WebServlet("/resetPassword")
public class ResetPassword extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int uid = Integer.parseInt(request.getParameter("uid"));
            String name = Query.getUserNameByID(uid);
            String password = request.getParameter("password");
            String hashPassword = Hasher.hashPassword(name, password);
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.updatePassword);
            st.setString(1, hashPassword);
            st.setInt(2, uid);
            st.executeUpdate();
            response.getWriter().print("Password updated successfully.");
        } catch (SQLException e) {
            response.getWriter().print(e.getMessage());
            e.printStackTrace();
        }
    }

}
