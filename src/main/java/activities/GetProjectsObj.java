package activities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import models.Project;
import service.DBUtil.DatabaseConnection;
import service.DBUtil.Query;

public class GetProjectsObj {

    public static JSONObject getProjects(String query, String uid) {
        JSONObject jsonObject = new JSONObject();
        try {
            Connection con;
            con = DatabaseConnection.getDatabase();
            PreparedStatement st = con.prepareStatement(query);
            if (uid != "")
                st.setString(1, uid);
            ResultSet r1 = st.executeQuery();
            List<Project> projects = new ArrayList<Project>();
            while (r1.next()) {
                Project project = new Project();
                project.setP_id(r1.getString("p_id"));
                project.setTeam(r1.getString("team"));
                project.setTasks(r1.getString("tasks"));
                int oid = r1.getInt("owner_id");
                if (oid == 0)
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
                projects.add(project);
            }
            jsonObject.put("projects", projects);
            System.out.println(jsonObject);
            return jsonObject;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
