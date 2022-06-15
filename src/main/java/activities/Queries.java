package activities;

public class Queries {
    // GET users
    public static String getUsers = "SELECT * FROM user";
    public static String getUserNameByID = "SELECT name FROM user WHERE u_id=?";
    public static String getUserByName = "SELECT * FROM user WHERE name=?";
    public static String getUserByEmail = "SELECT * FROM user WHERE email=?";
    public static String getUserNameEmailByID = "SELECT name, email FROM user WHERE u_id=?";
    public static String getUserIDByName = "SELECT u_id FROM user WHERE name=?";
    public static String getUserIDName = "SELECT u_id, name FROM user";

    // Delete users
    public static String deleteUser = "DELETE FROM user WHERE u_id=?";

    // GET projects
    public static String getProjects = "SELECT * FROM projects";
    public static String getProjectByName = "SELECT * FROM projects WHERE name=?";
    public static String getProjectByID = "SELECT name, description, owner_id from projects WHERE p_id=?";
    public static String getProjectMembers = "SELECT u_id, o_id FROM project_users p1 WHERE p1.p_id = ?";

    // GET task
    public static String getTasks = "SELECT * FROM tasks";
    public static String getTaskByID = "SELECT * FROM tasks where t_id=?";
    public static String getTaskCount = "SELECT COUNT(t_id) AS tasks FROM tasks WHERE p_id=?;";

    // SET task
    public static String setTaskStatus = "UPDATE tasks SET completed = ? WHERE t_id = ?;";

    // UPDATE task
    public static String updateTask = "UPDATE tasks SET title = ?, description = ?, start_date = ?, end_date = ?, priority = ?, assignee_id = ?, completed = ? WHERE t_id = ?;";

    // DELETE tasks
    public static String deleteTask = "DELETE FROM tasks WHERE t_id = ?;";

    // GET project_users
    public static String uidInProUsers = "SELECT sno, u_id from project_users where p_id=?;";
    public static String getUsersByPID = "SELECT u_id from project_users where p_id=?;";
    public static String getUserCountOnProject = "SELECT COUNT('u_id') AS team FROM project_users WHERE p_id=?;";

    // INSERT projects
    public static String putIntoProject = "INSERT INTO projects (name, description, owner_id) VALUES (?, ?, ?)";
    public static String addMembers = """
                    INSERT INTO project_users (p_id, u_id, o_id)
                    SELECT * FROM (SELECT ? AS p_id, ? AS u_id, ? AS o_id) AS tmp
                    WHERE NOT
                    EXISTS (
                        SELECT p_id, u_id FROM project_users WHERE p_id = ? AND u_id = ?
                    ) LIMIT 1;
            """;

    // UPDATE users
    public static String updateUserByID = "UPDATE user SET name=?, email=?, role=? WHERE u_id=?";

    // DELETE project_users
    public static String deleteProUser = "DELETE FROM project_users WHERE sno = ?;";

    // MISC
    public static String updatePassword = "UPDATE user SET password = ? WHERE u_id = ?";

}
