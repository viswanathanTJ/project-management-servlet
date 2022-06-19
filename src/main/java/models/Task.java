package models;

public class Task {
    private String assignee;
    private String cname;
    private String completed;
    private String priority;
    private String project;
    private String start_date;
    private String t_id;
    private String title;

    public Task() {
    }

    public Task(String assignee, String cname, String completed, String priority, String project, String start_date,
            String t_id, String title) {
        this.assignee = assignee;
        this.cname = cname;
        this.completed = completed;
        this.priority = priority;
        this.project = project;
        this.start_date = start_date;
        this.t_id = t_id;
        this.title = title;
    }

    public String getAssignee() {
        return this.assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getCname() {
        return this.cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCompleted() {
        return this.completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getProject() {
        return this.project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getStart_date() {
        return this.start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getT_id() {
        return this.t_id;
    }

    public void setT_id(String t_id) {
        this.t_id = t_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}