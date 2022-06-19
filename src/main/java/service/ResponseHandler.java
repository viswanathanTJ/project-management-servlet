package service;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

public class ResponseHandler {

    protected static void successResponse(HttpServletResponse response, JSONObject jsonObj) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(jsonObj);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void successResponse(HttpServletResponse response, String msg) {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        try {
            response.getWriter().print(msg);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void errorResponse(HttpServletResponse response, int scode, String message) {
        response.setStatus(scode);
        try {
            response.getWriter().print(message);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
