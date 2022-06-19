package activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import queries.Queries;
import service.DBUtil.DatabaseConnection;
import service.ResponseHandler;

public class AdminGetActions extends ResponseHandler {

    public void getAllCounts(HttpServletRequest request, HttpServletResponse response) {
        try {
            Connection con = DatabaseConnection.getDatabase();
            PreparedStatement st;
            ResultSet r1;
            st = con.prepareStatement(Queries.getAllCounts);
            r1 = st.executeQuery();
            JSONObject obj = new JSONObject();
            if (r1.next()) {
                obj.put("projects", r1.getInt("projects"));
                obj.put("users", r1.getInt("users"));
                obj.put("tasks", r1.getInt("tasks"));
                obj.put("open", r1.getInt("open"));
            }
            successResponse(response, obj);
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            e.printStackTrace();
        }
    }

    public void getRecentUsers(HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println("GetRecentUsers");
            Connection con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.getRecentUsers);
            ResultSet r1 = st.executeQuery();
            JSONObject jsonObject = new JSONObject();
            JSONArray array = new JSONArray();
            while (r1.next()) {
                JSONObject obj = new JSONObject();
                obj.put("name", r1.getString("name"));
                obj.put("role", r1.getString("role"));
                array.put(obj);
            }
            jsonObject.put("users", array);
            successResponse(response, jsonObject);
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
            e.printStackTrace();
        }
    }

    public void getRecentTasks(HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println("GetRecentTasks");
            Connection con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.getRecentTasks);
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
                array.put(obj);
            }
            jsonObject.put("tasks", array);
            successResponse(response, jsonObject);
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
            e.printStackTrace();
        }
    }

}
