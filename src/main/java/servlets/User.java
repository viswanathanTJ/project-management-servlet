package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.UserGetActions;

@WebServlet("/User/*")
public class User extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		System.out.println(uri);
		String[] uriSplit = uri.split("/");
		String action = uriSplit[uriSplit.length - 1];
		System.out.println(action);
		UserGetActions user = new UserGetActions();
		switch (action) {
			case "getRecentTasks":
				user.getRecentTasks(request, response);
				break;
			case "getRecentInfo":
				user.getRecentInfo(request, response);
				break;
			case "getProjects":
				user.getProjects(request, response);
				break;
			case "getTasks":
				user.getTasks(request, response);
				break;
			case "getUserID":
				user.getUserID(request, response);
				break;
			default:
				response.setStatus(404);
				break;
		}
	}

}
