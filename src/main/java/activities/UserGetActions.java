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

import queries.Queries;
import service.ResponseHandler;
import service.SessionHandler;
import service.DBUtil.DatabaseConnection;

public class UserGetActions extends ResponseHandler {

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
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_NOT_ACCEPTABLE, e.getMessage());
            e.printStackTrace();
        }
    }

    public void getProjects(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SessionHandler session = new SessionHandler(request);
        String uid = session.getUID();
        response.setContentType("application/json");
        response.getWriter().print(GetProjectsObj.getProjects(Queries.getProjectByIDDetails, uid));
        response.setStatus(200);
    }

    public void getTasks(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SessionHandler session = new SessionHandler(request);
        String uid = session.getUID();
        System.out.println("User ID is " + uid);
        JSONObject json = GetTasksObj.getTasks(Queries.getTasksForUser, uid);
        System.out.println(json);
        if (json != null) {
            successResponse(response, json);
        } else {
            errorResponse(response, HttpServletResponse.SC_NOT_ACCEPTABLE, "No tasks found");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void getUserID(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SessionHandler session = new SessionHandler(request);
        String uid = session.getUID();
        response.getWriter().print(uid);
    }
}
