package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.EmployeeActions;

@WebServlet("/Employee/*")
public class Employee extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		System.out.println(uri);
		String[] uriSplit = uri.split("/");
		String action = uriSplit[uriSplit.length - 1];
		System.out.println(action);
		EmployeeActions emp = new EmployeeActions();
		switch (action) {
			case "getRecentTasks":
				emp.getRecentTasks(request, response);
				break;
			case "getRecentInfo":
				emp.getRecentInfo(request, response);
				break;
			case "getProjects":
				emp.getProjects(request, response);
				break;
			default:
				response.setStatus(404);
				break;
		}
	}

}
