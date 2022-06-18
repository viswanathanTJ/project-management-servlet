package models;

public class Project {
    private String name;
    private String desc;
    private String oid;
    private String oname;
    private String createdat;
    private String p_id;
    private String priority;
    private String tasks;
    private String team;

    public Project() {
    }

    public Project(String name, String desc, String oid, String oname, String created, String p_id, String priority,
            String tasks, String team) {
        super();
        this.name = name;
        this.desc = desc;
        this.oid = oid;
        this.oname = oname;
        this.createdat = created;
        this.p_id = p_id;
        this.priority = priority;
        this.tasks = tasks;
        this.team = team;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getoid() {
        return this.oid;
    }

    public void setoid(String oid) {
        this.oid = oid;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOname() {
        return this.oname;
    }

    public void setOname(String oname) {
        this.oname = oname;
    }

    public String getCreatedat() {
        return this.createdat;
    }

    public void setCreatedat(String created) {
        this.createdat = created;
    }

    public String getP_id() {
        return this.p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTasks() {
        return this.tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getTeam() {
        return this.team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

}
