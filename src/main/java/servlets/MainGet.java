package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.MainGetter;
import service.GetAction;

@WebServlet("/Get/*")
public class MainGet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private MainGetter getter = new MainGetter();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		String action = GetAction.get(request);
		System.out.println("Main Getter: " + action);
		switch (action) {
			case "getUsers":
				getter.getUsers(request, response);
				break;
			case "getProject":
				getter.getProject(request, response);
				break;
			case "getProjects":
				getter.getProjects(request, response);
				break;
			case "getTaskByID":
				getter.getTaskByID(request, response);
				break;
			case "getTasks":
				getter.getTasks(request, response);
				break;
			case "getUsersByPID":
				getter.getUsersByPID(request, response);
				break;
			case "getUsersInProject":
				getter.getUsersInProject(request, response);
				break;
			case "getProjectMembers":
				getter.getProjectMembers(request, response);
				break;
			default:
				response.setStatus(404);
				break;
		}
	}
}
