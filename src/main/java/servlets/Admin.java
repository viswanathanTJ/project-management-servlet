package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.AdminGetActions;
import service.GetAction;

@WebServlet("/Admin/*")
public class Admin extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private AdminGetActions adminActions = AdminGetActions.getInstance();

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		String action = GetAction.get(request);
		System.out.println("Admin: " + action);
		switch (action) {
			case "getAllCounts":
				adminActions.getAllCounts(request, response);
				break;
			case "getRecentUsers":
				adminActions.getRecentUsers(request, response);
				break;
			case "getRecentTasks":
				adminActions.getRecentTasks(request, response);
				break;
			case "getUsers":
				adminActions.getUsers(request, response);
				break;
			case "getProjects":
				adminActions.getProjects(request, response);
				break;
			default:
				response.setStatus(404);
				break;
		}
	}
}
