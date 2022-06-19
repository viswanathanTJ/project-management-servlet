package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.Authentication;
import service.GetAction;

@WebServlet("/Auth/*")
public class Auth extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private Authentication auth = Authentication.getInstance();

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = GetAction.get(request);
		switch (action) {
			case "register":
				auth.UserRegister(request, response);
				break;
			case "login":
				auth.UserLogin(request, response);
				break;
			case "logout":
				auth.UserLogout(request);
				break;
			default:
				response.setStatus(404);
				break;
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		auth.UserCheck(request, response);
	}
}
