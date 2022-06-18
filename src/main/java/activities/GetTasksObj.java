package activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import activities.DBUtil.DatabaseConnection;
import activities.DBUtil.Query;

public class GetTasksObj {

    public static JSONObject getTasks(String query, String uid) {
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        System.out.println(query + " " + uid);
        try {
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(query);
            if (uid != null)
                st.setString(1, uid);
            ResultSet r1 = st.executeQuery();
            while (r1.next()) {
                JSONObject obj = new JSONObject();
                obj.put("t_id", r1.getInt("t_id"));
                obj.put("title", r1.getString("title"));
                obj.put("desc", r1.getString("description"));
                obj.put("start_date", r1.getString("start_date").substring(0, 10));
                obj.put("due_date", r1.getString("end_date").substring(0, 10));
                obj.put("priority", r1.getString("priority"));
                obj.put("assignee_id", r1.getInt("assignee_id"));
                obj.put("completed", r1.getInt("completed"));
                obj.put("assignee", Query.getUserNameByID(r1.getInt("assignee_id")));
                obj.put("p_id", r1.getString("p_id"));
                obj.put("project", Query.getProjectNameByID(r1.getString("p_id")));
                obj.put("c_id", r1.getInt("creator_id"));
                obj.put("cname", Query.getUserNameByID(r1.getInt("creator_id")));
                obj.put("desc", r1.getString("description"));
                Timestamp t = r1.getTimestamp("createdat");
                SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                String time = df.format(t);
                obj.put("createdat", time);
                array.put(obj);
            }
            jsonObject.put("tasks", array);
            return jsonObject;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
