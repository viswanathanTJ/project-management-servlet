package queries;

public class Queries {
        // GET all counts
        public static final String getAllCounts = "SELECT (SELECT COUNT(*) FROM user) AS users, (SELECT COUNT(*) FROM tasks) AS tasks, (SELECT COUNT(*) FROM projects) AS projects, (SELECT COUNT(*) FROM tasks WHERE completed = 0) AS open FROM dual;";
        public static final String getUserCounts = "SELECT (SELECT COUNT(*) FROM tasks WHERE assignee_id=?) AS tasks, (SELECT COUNT(*) FROM project_users WHERE u_id=?) AS projects, (SELECT COUNT(*) FROM tasks WHERE completed = 0 AND assignee_id=?) AS open FROM dual;";

        // GET user
        public static String getUsers = "SELECT * FROM user";
        public static String getUserNameByID = "SELECT name FROM user WHERE u_id=?";
        public static String getUserByName = "SELECT * FROM user WHERE name=?";
        public static String getUserByEmail = "SELECT * FROM user WHERE email=?";
        public static String getUserNameEmailByID = "SELECT name, email FROM user WHERE u_id=?";
        public static String getUserIDByName = "SELECT u_id FROM user WHERE name=?";
        public static String getUserIDName = "SELECT u_id, name FROM user";
        public static String getRecentUsers = "SELECT name, role FROM user ORDER BY u_id DESC LIMIT 5;";

        // INSERT user
        public static String addUser = "INSERT INTO USER (name, email, password, role) VALUES (?, ?, ?, ?)";

        // Delete user
        public static String deleteUser = "DELETE FROM user WHERE u_id=?";

        // GET projects
        public static String getProjects = "SELECT * FROM projects";
        public static String getProjectsDetails = """
                        SELECT *, (SELECT COUNT(*) FROM project_users WHERE p_id=p.p_id) AS team,
                            (SELECT COUNT(*) AS tasks FROM tasks WHERE p_id=p.p_id AND completed = 0) AS tasks
                            FROM projects p;
                        """;
        public static String getProjectByName = "SELECT * FROM projects WHERE name=?";
        public static String getProjectByID = "SELECT * from projects WHERE p_id=?";
        public static String getProjectByIDDetails = """
                        SELECT p.p_id, p.name, p.description, p.priority, p.owner_id, p.createdat,
                        (SELECT COUNT(*) FROM project_users WHERE p_id=p.p_id) AS team,
                        (SELECT COUNT(*) AS tasks FROM tasks WHERE p_id=p.p_id AND completed = 0) AS tasks
                        FROM project_users pu INNER JOIN projects p on pu.p_id=p.p_id WHERE pu.u_id = ?;
                        """;
        public static String getProjectMembers = "SELECT u_id, o_id FROM project_users p1 WHERE p1.p_id = ?";

        // UPDATE projects
        public static String delOwnerOnProject = "UPDATE projects SET owner_id=0 WHERE p_id=?;";

        // DELETE projects
        public static String deleteProject = "DELETE FROM projects WHERE p_id=?;";

        // GET task
        public static String getTasks = "SELECT * FROM tasks";
        public static String getTasksForUser = "SELECT * FROM tasks WHERE assignee_id=?";
        public static String getTaskByID = "SELECT * FROM tasks where t_id=?";
        public static String getTaskCount = "SELECT COUNT(t_id) AS tasks FROM tasks WHERE p_id=? AND completed = 0;";
        public static String getRecentTasks = """
                        SELECT t.title, (SELECT value FROM priority WHERE t.priority+1=number) AS priority,
                        (SELECT name FROM user WHERE u_id = t.creator_id) AS owner,
                        (SELECT name FROM user WHERE u_id = t.assignee_id) AS assignee,
                        (SELECT name FROM projects WHERE p_id = t.p_id) AS project FROM tasks t
                            ORDER BY t_id DESC LIMIT 7;
                        """;
        public static String getRecentTasksForUser = """
                        SELECT t.title, (SELECT value FROM priority WHERE t.priority+1=number) AS priority,
                        (SELECT name FROM user WHERE u_id = t.creator_id) AS owner,
                        (SELECT name FROM user WHERE u_id = t.assignee_id) AS assignee,
                        (SELECT name FROM projects WHERE p_id = t.p_id) AS project, createdat FROM tasks t
                            WHERE t.assignee_id = ? ORDER BY t_id DESC LIMIT 7;
                        """;

        // SET task
        public static String setTaskStatus = "UPDATE tasks SET completed = ? WHERE t_id = ?;";

        // UPDATE task
        public static String updateTask = """
                        UPDATE tasks SET title = ?, description = ?, start_date = ?, end_date = ?, priority = ?,
                        assignee_id = ?, completed = ? WHERE t_id = ?;
                        """;

        // DELETE tasks
        public static String deleteTask = "DELETE FROM tasks WHERE t_id = ?;";

        // GET project_users
        public static String uidInProUsers = "SELECT sno, u_id from project_users where p_id=?;";
        public static String getUsersByPID = "SELECT u_id from project_users where p_id=?;";
        public static String getUserCountOnProject = "SELECT COUNT('u_id') AS team FROM project_users WHERE p_id=?;";
        public static String getProjectsForUser = "SELECT * from project_users WHERE u_id=?;";

        // DELETE project_users
        public static String deleteProjectUsers = "DELETE FROM project_users WHERE p_id = ?;";

        // INSERT projects
        public static String putIntoProject = "INSERT INTO projects (name, description, priority, owner_id) VALUES (?, ?, ?, ?)";
        public static String updateProject = "UPDATE projects SET name=?, description=?, owner_id=? WHERE p_id=?;";
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
