package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import activities.GetProjectsObj;
import queries.Queries;

@WebServlet("/getProjects")
public class GetProjects extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		response.getWriter().print(GetProjectsObj.getProjects(Queries.getProjectsDetails, ""));
		response.setStatus(200);
	}

}
