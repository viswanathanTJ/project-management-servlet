package entities;

public class Project {
    private String name;
    private String desc;
    private String oid;
    private String oname;
    private String created;
    
    public Project(String name, String desc, String oid, String oname, String created) {
        super();
        this.name = name;
        this.desc = desc;
        this.oid = oid;
        this.oname = oname;
        this.created = created;
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
    
    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
