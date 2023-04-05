package pkg817_project_sockets;
import javax.crypto.*;
import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.*;
import java.security.*;
import javax.crypto.Cipher;
import javax.crypto.spec.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;    
import java.security.MessageDigest;
import java.util.Arrays;

public class RSA {
    private static PrivateKey privateKey;
    private static PublicKey publicKey;
    public static void generateKeyPair(String host) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair pair = generator.generateKeyPair();

            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        }catch(Exception e){System.out.println(e);}
        try (FileOutputStream fos = new FileOutputStream("public" + host + ".key")) {
            fos.write(publicKey.getEncoded());
            fos.close();
        }catch(Exception e){System.out.println(e);}
        try (FileOutputStream fos = new FileOutputStream("private" + host + ".key")) {
            fos.write(privateKey.getEncoded());
            fos.close();
        }catch(Exception e){System.out.println(e);}
    }
        
    public static String encryptPriv(String message, String host) {
        
        try {
            File KeyFile = new File("private" + host + ".key");
            byte[] KeyBytes = Files.readAllBytes(KeyFile.toPath());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            
            Cipher cipher = Cipher.getInstance("RSA");
            
            EncodedKeySpec KeySpec = new PKCS8EncodedKeySpec(KeyBytes);
            PrivateKey key = keyFactory.generatePrivate(KeySpec);
            
            cipher.init(Cipher.ENCRYPT_MODE, key);
            
            
            byte[] messageBytes = message.getBytes();
            byte[] cipherText = cipher.doFinal(messageBytes);
           
            String result = Base64.getEncoder().encodeToString(cipherText);
           
            return result;

        }
        catch(Exception e) {
            // null
        }

        return null;
        
    }
    
    public static String decryptPriv(String inputLine, String host) {
        
        try {
            File KeyFile = new File("public" + host + ".key");
            byte[] KeyBytes = Files.readAllBytes(KeyFile.toPath());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            
            Cipher cipher = Cipher.getInstance("RSA");
        

            EncodedKeySpec KeySpec = new X509EncodedKeySpec(KeyBytes);
            PublicKey key = keyFactory.generatePublic(KeySpec);

            cipher.init(Cipher.DECRYPT_MODE, key);
            

            byte[] decryptedMessageBytes = cipher.doFinal(Base64.getDecoder().decode(inputLine));
            String decryptedMessage = new String(decryptedMessageBytes);
            
            return decryptedMessage;

        }
        catch(Exception e) {
                System.out.println("An error occurred: ");
                e.printStackTrace();
        }

        return null;
        
    }

    public static String encryptPub(String message, String host) {
        try {
            File KeyFile = new File("public" + host + ".key");
            byte[] KeyBytes = Files.readAllBytes(KeyFile.toPath());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            
            Cipher cipher = Cipher.getInstance("RSA");
            
            EncodedKeySpec KeySpec = new X509EncodedKeySpec(KeyBytes);
            PublicKey key = keyFactory.generatePublic(KeySpec);
            
            cipher.init(Cipher.ENCRYPT_MODE, key);
            
            
            byte[] messageBytes = message.getBytes();
            //byte[] mb1 = Arrays.copyOfRange(messageBytes, 0, messageBytes.length/2);
            //byte[] mb2 = Arrays.copyOfRange(messageBytes, messageBytes.length/2, messageBytes.length);

            //byte[] cipherText1 = cipher.doFinal(mb1);
            //byte[] cipherText2 = cipher.doFinal(mb2);
            byte[] cipherText = cipher.doFinal(messageBytes);
            //byte[] cipherText = new byte[cipherText1.length + cipherText2.length];
            //System.arraycopy(cipherText1, 0, cipherText, 0, cipherText1.length);
            //System.arraycopy(cipherText2, 0, cipherText, 0, cipherText2.length);
            String result = Base64.getEncoder().encodeToString(cipherText);
            
            return result;

        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return null;
        
    }
    
    public static String decryptPub(String inputLine, String host) {
        
        try {
            File KeyFile = new File("private" + host + ".key");
            byte[] KeyBytes = Files.readAllBytes(KeyFile.toPath());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            
            Cipher cipher = Cipher.getInstance("RSA");
            EncodedKeySpec KeySpec = new PKCS8EncodedKeySpec(KeyBytes);
            PrivateKey key = keyFactory.generatePrivate(KeySpec);
            cipher.init(Cipher.DECRYPT_MODE, key);
            

            byte[] decryptedMessageBytes = cipher.doFinal(Base64.getDecoder().decode(inputLine));
            String decryptedMessage = new String(decryptedMessageBytes);
            
            return decryptedMessage;

        }
        catch(Exception e) {
                System.out.println("An error occurred: ");
                e.printStackTrace();
        }

        return null;
        
    }
}