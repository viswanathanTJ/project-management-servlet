package service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionHandler {
    public HttpSession session;
    protected String uid;
    protected String name;
    protected String email;
    protected String role;
    protected int interval;
    
    public SessionHandler(HttpServletRequest request, String uid, String name, String email, String role,
            int interval) {
        this.session = request.getSession();
        session.setAttribute("uid", uid);
        session.setAttribute("name", name);
        session.setAttribute("email", email);
        session.setAttribute("role", role);
        session.setMaxInactiveInterval(interval);
    }

    public SessionHandler(HttpServletRequest request) {
        this.session = request.getSession();
    }

    public String getRole() {
        return (String) session.getAttribute("role");
    }

    public void setRole(String role) {
        session.setAttribute("role", role);
    }

    public String getEmail() {
        return (String) session.getAttribute("email");
    }

    public void setEmail(String email) {
        session.setAttribute("email", email);
    }

    public String getUID() {
        return (String) session.getAttribute("uid");
    }

    public void setUID(String uid) {
        session.setAttribute("uid", uid);
    }

    public String getName() {
        return (String) session.getAttribute("name");
    }

    public void setName(String name) {
        session.setAttribute("name", name);
    }

    public int getInterval() {
        return (int) session.getMaxInactiveInterval();
    }

    public void setInterval(int val) {
        session.setMaxInactiveInterval(val);
    }

    public void invalidateSession() {
        session.invalidate();
    }

}
