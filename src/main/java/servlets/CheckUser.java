package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.SessionHandler;

@WebServlet("/check")
public class CheckUser extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SessionHandler session = new SessionHandler(request);
		String role = session.getRole();
		if (role == null || role == "")
			response.getWriter().print("login");
		else
			response.getWriter().print(role);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String role = request.getParameter("role");
		response.getWriter().print(ResponseHandler.decrypt(role));
	}
}
