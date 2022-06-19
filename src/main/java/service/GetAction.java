package service;

import javax.servlet.http.HttpServletRequest;

public class GetAction {
    public static String get(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String[] uriSplit = uri.split("/");
        String action = uriSplit[uriSplit.length - 1];
        return action;
    }
}
