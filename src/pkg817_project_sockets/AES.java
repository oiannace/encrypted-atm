package pkg817_project_sockets;
import javax.crypto.*;
import java.net.*;
import java.io.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.*;
import javax.crypto.Cipher;
import javax.crypto.spec.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;    
import java.security.MessageDigest;
import java.util.Arrays;

public class AES {
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void getKeyFromPassword(final String password) {
        MessageDigest sha = null;
        try {
            key = password.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
            
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
    }

    public static String encrypt(String message, String password) {
        
        try {
            getKeyFromPassword(password);

            // Iniiate DES cypher
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
            
            String result = Base64.getEncoder().encodeToString(cipherText);
            return result;

        }
        catch(Exception e) {
            // null
        }

        return null;
        
    }
    
    public static String decrypt(String inputLine, String password) {
        
        try {
            getKeyFromPassword(password);
            
            // Initiate DES cypher
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] inputByteDec = cipher.doFinal(Base64.getDecoder().decode(inputLine));
            
            return new String(inputByteDec);

        }
        catch(Exception e) {
                System.out.println("An error occurred: ");
                e.printStackTrace();
        }

        return null;
        
    }
}