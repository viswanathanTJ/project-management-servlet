package activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONObject;

import models.Task;
import service.DBUtil.DatabaseConnection;

public class GetTasksObj {

    public static JSONObject getTasks(String query, String uid) {
        JSONObject jsonObject = new JSONObject();
        System.out.println(query + " " + uid);
        ArrayList<Task> tasks = new ArrayList<Task>();
        try {
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(query);
            if (uid != null) {
                st.setString(1, uid);
                st.setString(2, uid);
            }
            ResultSet r1 = st.executeQuery();
            while (r1.next()) {
                Task task = new Task();
                task.setT_id(r1.getString("t_id"));
                task.setTitle(r1.getString("title"));
                task.setStart_date(r1.getString("start_date").substring(0, 10));
                task.setPriority(r1.getString("priority"));
                task.setCompleted(r1.getString("completed"));
                task.setAssignee(r1.getString("assignee"));
                task.setProject(r1.getString("project"));
                task.setCname(r1.getString("cname"));
                tasks.add(task);
            }
            jsonObject.put("tasks", tasks);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
