package activities;

public class Queries {
    public static String addMembers = """
                    INSERT INTO project_users (p_id, u_id, o_id)
                    SELECT * FROM (SELECT ?
                        AS p_id,?
                        AS u_id,?
                        AS o_id)
                        AS tmp
                        WHERE NOT
                        EXISTS (
                        SELECT p_id, u_id FROM project_users WHERE p_id = ? AND u_id = ?
                    ) LIMIT 1;
            """;

    public static String uidInProUsers = "SELECT sno, u_id from project_users where p_id=?;";
    public static String deleteProUser = "DELETE FROM project_users WHERE sno = ?;";
}
