package servlets;

import java.io.IOException;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;

public class ResponseHandler {

    private static String key = "nZr4u7x!A%D*G-Ka";

    public static void successResponse(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(msg);
        response.getWriter().flush();
    }

    public static void errorResponse(HttpServletResponse response, int scode, String message) throws IOException {
        response.setStatus(scode);
        response.getWriter().print(message);
        return;
    }

    public static String encrypt(String in) {
        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(in.getBytes());
            String encoded = Base64.getEncoder().encodeToString(encrypted);
            return encoded;
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
        return "null";
    }

    public static String decrypt(String in) {
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        byte[] barr = Base64.getDecoder().decode(in);
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
            String decrypted = new String(cipher.doFinal(barr));
            return decrypted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
