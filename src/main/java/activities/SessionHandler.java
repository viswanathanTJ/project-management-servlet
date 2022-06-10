package activities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionHandler {
    public HttpSession session;
    protected String role;
    protected String name;
    protected String uid;
    protected String email;

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

    public void setInterval(int val) {
        session.setMaxInactiveInterval(val);
    }

    public void invalidateSession() {
        session.invalidate();
    }

}
