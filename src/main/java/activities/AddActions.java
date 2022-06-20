package activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import models.Task;
import models.User;
import queries.Queries;
import service.DBUtil.DatabaseConnection;
import service.DBUtil.Query;
import service.Hasher;
import service.ResponseHandler;
import service.SessionHandler;

public class AddActions extends ResponseHandler {

    private static final AddActions instance = new AddActions();

    private Connection con;
    private PreparedStatement st;
    private ResultSet r1;

    private AddActions() {
        this.con = DatabaseConnection.getDatabase();
    }

    public static AddActions getInstance() {
        return instance;
    }

    private String getGeneratedID() throws SQLException {
        int row = st.executeUpdate();
        if (row == 0)
            throw new SQLException("Creating failed, no rows affected.");
        String id;
        try (ResultSet generatedKeys = st.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                id = generatedKeys.getString(1);
                System.out.println("Created with ID = " + generatedKeys.getString(1));
            } else
                throw new SQLException("Creating failed, no ID obtained.");
        }
        return id;
    }

    public void addProject(HttpServletRequest request, HttpServletResponse response) {
        try {
            String name = request.getParameter("name");
            String desc = request.getParameter("desc");
            String priority = request.getParameter("priority");
            System.out.println("Add Project: " + name + " " + desc + " " + priority);
            String uid;
            SessionHandler session = new SessionHandler(request);

            String oid = session.getUID();
            String owner = session.getName();
            System.out.println("Owner ID " + oid + " Owner Name " + owner);

            st = con.prepareStatement(Queries.getProjectByName);
            st.setString(1, name);
            r1 = st.executeQuery();
            if (r1.next()) {
                errorResponse(response, HttpServletResponse.SC_NOT_ACCEPTABLE, "Project already exists");
                return;
            }
            st = con.prepareStatement(Queries.putIntoProject, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, name);
            st.setString(2, desc);
            st.setString(3, priority);
            st.setString(4, oid);
            System.out.println("Project added");
            String pid = getGeneratedID();
            System.out.println("Project ID generated is: " + pid);
            // Add Members
            Enumeration<String> parameterNames = request.getParameterNames();
            HashSet<Integer> set = new HashSet<Integer>();

            int affected = 0;
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                if (paramName.equals("name") || paramName.equals("desc") || paramName.equals("priority"))
                    continue;
                System.out.println("User ID: " + paramName);
                uid = request.getParameter(paramName);
                st = con.prepareStatement(Queries.addMembers);
                set.add(Integer.parseInt(uid));
                System.out.println(uid);
                st.setString(1, pid);
                st.setString(2, uid);
                st.setString(3, oid);
                st.setString(4, pid);
                st.setString(5, uid);
                affected = st.executeUpdate();
            }
            System.out.println("Added: " + affected + " employees to project " + pid);
            successResponse(response, pid + "");
        } catch (SQLException e) {
            e.printStackTrace();
            errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public void addTask(HttpServletRequest request, HttpServletResponse response) {
        try {
            String title = request.getParameter("title");
            String desc = request.getParameter("description");
            String startDate = request.getParameter("start_date");
            String endDate = request.getParameter("end_date");
            String priority = request.getParameter("priority");
            String assigneeID = request.getParameter("assignee");
            String assignee = Query.getUserNameByID(assigneeID);
            String pid = request.getParameter("project");
            SessionHandler session = new SessionHandler(request);
            System.out
                    .println("Add Task: " + title + " " + desc + " " + startDate + " " + endDate + " " + priority + " "
                            + assignee + " " + pid);
            st = con.prepareStatement("SELECT title from tasks");
            r1 = st.executeQuery();
            while (r1.next()) {
                if (r1.getString("title").equals(title)) {
                    errorResponse(response, HttpServletResponse.SC_NOT_ACCEPTABLE, "Task already exists");
                    return;
                }
            }
            String creatorID = session.getUID();
            String cname = session.getName();

            st = con.prepareStatement(Queries.addTask, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, title);
            st.setString(2, desc);
            st.setString(3, startDate);
            st.setString(4, endDate);
            st.setString(5, priority);
            st.setString(6, assigneeID);
            st.setString(7, creatorID);
            st.setString(8, pid);
            st.setInt(9, 0);

            // Run Query
            String tid = getGeneratedID();
            String project = Query.getProjectNameByID(pid);
            Task task = new Task(assignee, cname, "0", priority, project, startDate, tid, title);

            System.out.println(new Gson().toJson(task));
            successResponse(response, new Gson().toJson(task));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addUser(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            System.out.println("Add User: " + username + " " + email + " " + password + " " + role);
            st = con.prepareStatement(Queries.getUserByEmail);
            st.setString(1, email);
            r1 = st.executeQuery();
            if (r1.next()) {
                errorResponse(response, HttpServletResponse.SC_NOT_ACCEPTABLE, "Mail ID already exists");
                return;
            }
            st = con.prepareStatement(Queries.getUserByName);
            st.setString(1, username);
            r1 = st.executeQuery();
            if (r1.next()) {
                errorResponse(response, HttpServletResponse.SC_NOT_ACCEPTABLE, "Username already exists");
                return;
            }
            st = con.prepareStatement(Queries.addUser, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, username);
            st.setString(2, email);
            st.setString(3, Hasher.hashPassword(username, password));
            st.setString(4, role);
            // Run Query
            String aid = getGeneratedID();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new Date());
            User user = new User(aid, username, email, role, currentTime);
            successResponse(response, new Gson().toJson(user));
        } catch (SQLException e) {
            errorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            e.printStackTrace();
        }
    }
}
