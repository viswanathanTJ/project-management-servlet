package activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import service.ResponseHandler;
import service.DBUtil.DatabaseConnection;
import queries.Queries;

public class AdminGetActions extends ResponseHandler {
    private static final AdminGetActions instance = new AdminGetActions();

    private Connection con;
    private PreparedStatement st;
    private ResultSet r1;

    private AdminGetActions() {
        this.con = DatabaseConnection.getDatabase();
    }

    public static AdminGetActions getInstance() {
        return instance;
    }

    public void getAllCounts(HttpServletRequest request, HttpServletResponse response) {
        try {
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
            st = con.prepareStatement(Queries.getRecentUsers);
            r1 = st.executeQuery();
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
            st = con.prepareStatement(Queries.getRecentTasks);
            r1 = st.executeQuery();
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

    public void getUsers(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.getUsers);
            ResultSet r1 = st.executeQuery();
            while (r1.next()) {
                JSONObject obj = new JSONObject();
                obj.put("uid", r1.getInt("u_id"));
                // obj.put("sno", sno++);
                obj.put("name", r1.getString("name"));
                obj.put("email", r1.getString("email"));
                obj.put("role", r1.getString("role"));
                Timestamp t = r1.getTimestamp("createdat");
                SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                String time = df.format(t);
                obj.put("createdat", time);
                array.put(obj);
            }
            jsonObject.put("users", array);
            successResponse(response, jsonObject);
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
            e.printStackTrace();
        }
    }

    public void getProjects(HttpServletRequest request, HttpServletResponse response) {
        JSONObject projects = GetProjectsObj.getProjects(Queries.getProjectsDetails, "");
        successResponse(response, projects);
    }
    
}
