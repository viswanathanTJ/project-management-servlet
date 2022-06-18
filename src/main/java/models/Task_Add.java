package models;

public class Task_Add {
    protected String creator;
    protected String title;
    protected String desc;
    protected String startDate;
    protected String endDate;
    protected int priority;
    protected String assigneeID;
    protected String assignee;
    protected int creatorID;
    protected String cname;
    protected String pid;
    protected String project;
    protected int completed;

    public Task_Add(String creator, String title, String desc, String startDate, String endDate, int priority,
            String assigneeID, String assignee, int creatorID, String cname, String pid, String project,
            int completed) {
        this.creator = creator;
        this.title = title;
        this.desc = desc;
        this.startDate = startDate;
        this.endDate = endDate;
        this.priority = priority;
        this.assigneeID = assigneeID;
        this.assignee = assignee;
        this.creatorID = creatorID;
        this.pid = pid;
        this.project = project;
        this.completed = completed;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getAssignee() {
        return this.assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getAssigneeID() {
        return this.assigneeID;
    }

    public void setAssigneeID(String assigneeID) {
        this.assigneeID = assigneeID;
    }

    public int getCreatorID() {
        return this.creatorID;
    }

    public void setCreatorID(int creatorID) {
        this.creatorID = creatorID;
    }

    public String getCname() {
        return this.cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getProject() {
        return this.project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getCompleted() {
        return this.completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
