package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.AddActions;
import service.GetAction;

@WebServlet("/Add/*")
public class MainAdd extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private AddActions adder = AddActions.getInstance();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		String action = GetAction.get(request);
		System.out.println("Main Adder: " + action);
		switch (action) {
			case "addProject":
				adder.addProject(request, response);
				break;
			case "addTask":
				adder.addTask(request, response);
				break;
			case "addUser":
				adder.addUser(request, response);
				break;
			default:
				response.setStatus(404);
				break;
		}
	}
}
