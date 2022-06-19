package activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import models.Project;
import queries.Queries;
import service.DBUtil.DatabaseConnection;
import service.DBUtil.Query;
import service.ResponseHandler;

public class MainGetter extends ResponseHandler {
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

    public void getProject(HttpServletRequest request, HttpServletResponse response) {
        String pid = request.getParameter("pid");
        try {
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.getProjectByIDDetails);
            st.setString(1, pid);
            ResultSet r1 = st.executeQuery();
            Project project = new Project();
            if (r1.next()) {
                project.setP_id(r1.getString("p_id"));
                project.setTeam(r1.getString("team"));
                project.setTasks(r1.getString("tasks"));
                String oid = r1.getString("owner_id");
                if (oid == null || oid == "")
                    project.setOname("unassigned");
                else
                    project.setOname(Query.getUserNameByID(oid));
                project.setName(r1.getString("name"));
                project.setDesc(r1.getString("description"));
                project.setPriority(r1.getString("priority"));
                Timestamp t = r1.getTimestamp("createdat");
                SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                String time = df.format(t);
                project.setCreatedat(time);
            }
            successResponse(response, new Gson().toJson(project));
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
            e.printStackTrace();
        }
    }

    public void getProjects(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = GetProjectsObj.getProjects(Queries.getProjectsDetails, "");
        successResponse(response, jsonObject);
    }

    public void getTaskByID(HttpServletRequest request, HttpServletResponse response) {
        String tid = request.getParameter("tid");
        try {
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.getTaskByID);
            st.setString(1, tid);
            ResultSet r1 = st.executeQuery();
            JSONObject obj = new JSONObject();
            if (r1.next()) {
                obj.put("t_id", r1.getInt("t_id"));
                obj.put("title", r1.getString("title"));
                obj.put("desc", r1.getString("description"));
                obj.put("start_date", r1.getString("start_date").substring(0, 10));
                obj.put("due_date", r1.getString("end_date").substring(0, 10));
                obj.put("priority", r1.getString("priority"));
                obj.put("assignee_id", r1.getInt("assignee_id"));
                obj.put("completed", r1.getInt("completed"));
                obj.put("assignee", Query.getUserNameByID(r1.getString("assignee_id")));
                obj.put("p_id", r1.getString("p_id"));
                obj.put("project_name", Query.getProjectNameByID(r1.getString("p_id")));
                obj.put("c_id", r1.getInt("creator_id"));
                obj.put("cname", Query.getUserNameByID(r1.getString("creator_id")));
                obj.put("desc", r1.getString("description"));
                Timestamp t = r1.getTimestamp("createdat");
                SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                String time = df.format(t);
                obj.put("createdat", time);
            }
            successResponse(response, obj);
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
            e.printStackTrace();
        }
    }

    public void getTasks(HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = GetTasksObj.getTasks(Queries.getTasksForTable, null);
        if (json != null)
            successResponse(response, json);
        else
            errorResponse(response, HttpServletResponse.SC_EXPECTATION_FAILED, "No tasks found");
    }

    public void getUsersInProject(HttpServletRequest request, HttpServletResponse response) {
        String pid = request.getParameter("pid");
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.getUsersByPID);
            st.setString(1, pid);
            ResultSet r1 = st.executeQuery();
            while (r1.next()) {
                JSONObject obj = new JSONObject();
                obj.put("uid", r1.getInt("u_id"));
                obj.put("name", Query.getUserNameByID(r1.getString("u_id")));
                array.put(obj);
            }
            jsonObject.put("users", array);
            successResponse(response, jsonObject);
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
            e.printStackTrace();
        }
    }

    public void getUsersByPID(HttpServletRequest request, HttpServletResponse response) {
        String pid = request.getParameter("pid");
        try {
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.getUsersByPID);
            st.setString(1, pid);
            ResultSet r1 = st.executeQuery();
            JSONObject jsonObject = new JSONObject();
            JSONArray array = new JSONArray();
            while (r1.next()) {
                JSONObject obj = new JSONObject();
                obj.put("uid", r1.getString("u_id"));
                obj.put("name", Query.getUserNameByID(r1.getString("u_id")));
                array.put(obj);
            }
            jsonObject.put("users", array);
            successResponse(response, jsonObject);
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
            e.printStackTrace();
        }
    }

    public void getProjectMembers(HttpServletRequest request, HttpServletResponse response) {
        String pid = request.getParameter("pid");
        System.out.println("GetProjectMembers " + pid);
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        HashSet<Integer> set = new HashSet<Integer>();
        boolean hasMembers = false;
        try {
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(Queries.getProjectMembers);
            st.setString(1, pid);
            ResultSet r1 = st.executeQuery();
            int oid = 1;
            while (r1.next()) {
                oid = r1.getInt("o_id");
                set.add(r1.getInt("u_id"));
            }
            st = con.prepareStatement(Queries.getUserIDName);
            r1 = st.executeQuery();
            while (r1.next()) {
                int uid = r1.getInt("u_id");
                if (uid == oid)
                    continue;
                JSONObject obj = new JSONObject();
                obj.put("uid", uid);
                obj.put("name", r1.getString("name"));
                if (set.contains(uid)) {
                    obj.put("isMember", "checked");
                    hasMembers = true;
                } else
                    obj.put("isMember", "");
                array.put(obj);
            }
            jsonObject.put("members", array);
            jsonObject.put("hasMembers", hasMembers);
            successResponse(response, jsonObject);
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_EXPECTATION_FAILED, e.getMessage());
            e.printStackTrace();
        }
    }

}
