package service;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Cryptor {

    private static String key = "nZr4u7x!A%D*G-Ka";

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
