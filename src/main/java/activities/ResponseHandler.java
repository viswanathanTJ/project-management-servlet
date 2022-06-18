package activities;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class ResponseHandler {
    public static void successResponse(HttpServletResponse response, JSONObject jsonObj) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(jsonObj);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void successResponse(HttpServletResponse response, String msg) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(msg);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void errorResponse(HttpServletResponse response, int scode, String message) {
        response.setStatus(scode);
        try {
            response.getWriter().print(message);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
