package activities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import activities.DBUtil.DatabaseConnection;

public class EmployeeActions extends ResponseHandler {

    public void getRecentTasks(HttpServletRequest request, HttpServletResponse response) {
        SessionHandler session = new SessionHandler(request);
        String uid = session.getUID();
        try {
            Connection con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.getRecentTasksForUser);
            st.setString(1, uid);
            ResultSet r1 = st.executeQuery();
            JSONObject jsonObject = new JSONObject();
            JSONArray array = new JSONArray();
            while (r1.next()) {
                JSONObject obj = new JSONObject();
                obj.put("title", r1.getString("title"));
                obj.put("priority", r1.getString("priority"));
                obj.put("owner", r1.getString("owner"));
                obj.put("assignee", r1.getString("assignee"));
                obj.put("project", r1.getString("project"));
                obj.put("createdat", r1.getString("createdat"));
                System.out.println(obj);
                array.put(obj);
            }
            jsonObject.put("tasks", array);
            successResponse(response, jsonObject);
        } catch (ClassNotFoundException | SQLException e) {
            errorResponse(response, HttpServletResponse.SC_NOT_ACCEPTABLE, e.getMessage());
            e.printStackTrace();
        }
    }

    public void getRecentInfo(HttpServletRequest request, HttpServletResponse response) {
        SessionHandler session = new SessionHandler(request);
        String uid = session.getUID();
        try {
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.getUserCounts);
            st.setString(1, uid);
            st.setString(2, uid);
            st.setString(3, uid);
            ResultSet r1 = st.executeQuery();
            JSONObject obj = new JSONObject();
            if (r1.next()) {
                obj.put("projects", r1.getInt("projects"));
                obj.put("tasks", r1.getInt("tasks"));
                obj.put("open", r1.getInt("open"));
            }
            successResponse(response, obj);
        } catch (ClassNotFoundException | SQLException e) {
            errorResponse(response, HttpServletResponse.SC_NOT_ACCEPTABLE, e.getMessage());
            e.printStackTrace();
        }
    }

    public void getProjects(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SessionHandler session = new SessionHandler(request);
        String uid = session.getUID();
        try {
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.getUserCounts);
            st.setString(1, uid);
            st.setString(2, uid);
            st.setString(3, uid);
            ResultSet r1 = st.executeQuery();
            JSONObject obj = new JSONObject();
            if (r1.next()) {
                obj.put("projects", r1.getInt("projects"));
                obj.put("tasks", r1.getInt("tasks"));
                obj.put("open", r1.getInt("open"));
            }
            successResponse(response, obj);
        } catch (ClassNotFoundException | SQLException e) {
            errorResponse(response, HttpServletResponse.SC_NOT_ACCEPTABLE, e.getMessage());
            e.printStackTrace();
        }
    }
}
