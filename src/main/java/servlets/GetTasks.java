package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import activities.GetTasksObj;
import activities.Queries;

@WebServlet("/getTasks")
public class GetTasks extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject json = GetTasksObj.getTasks(Queries.getTasks, null);
		if (json != null) {
			response.setContentType("application/json");
			response.getWriter().print(json);
			response.setStatus(200);
		} else {
			response.getWriter().print("Error at backend");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

}
