package models;

public class Task {
    protected String due_date;
    protected String cname;
    protected String project;
    protected String completed;
    protected String title;
    protected String priority;
    protected String t_id;
    protected String createdat;
    protected String c_id;
    protected String assignee;
    protected String p_id;
    protected String desc;
    protected String start_date;
    protected String assignee_id;

    public Task() {
    }

    public Task(String due_date, String cname, String project, String completed, String title, String priority,
            String t_id, String createdat, String c_id, String assignee, String p_id, String desc, String start_date,
            String assignee_id) {
        this.due_date = due_date;
        this.cname = cname;
        this.project = project;
        this.completed = completed;
        this.title = title;
        this.priority = priority;
        this.t_id = t_id;
        this.createdat = createdat;
        this.c_id = c_id;
        this.assignee = assignee;
        this.p_id = p_id;
        this.desc = desc;
        this.start_date = start_date;
        this.assignee_id = assignee_id;
    }

    public String getDue_date() {
        return this.due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getCname() {
        return this.cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getProject() {
        return this.project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getCompleted() {
        return this.completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getT_id() {
        return this.t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getCreatedat() {
        return this.createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public String getC_id() {
        return this.c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getAssignee() {
        return this.assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getP_id() {
        return this.p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStart_date() {
        return this.start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getAssignee_id() {
        return this.assignee_id;
    }

    public void setAssignee_id(String assignee_id) {
        this.assignee_id = assignee_id;
    }

}