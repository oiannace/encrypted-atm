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
import java.util.Calendar;
import java.util.Date;
import java.nio.file.*;
import java.security.*;
import java.util.Arrays;
import javax.swing.SwingUtilities;

public class Client {
    
    public static void main(String[] args) throws Exception {
        
        if (args.length != 3) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number> <client number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        int clientNumber = Integer.parseInt(args[2]);
        
        try (
            Socket echoSocket = new Socket(hostName, portNumber);
            PrintWriter out =
                new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
            BufferedReader stdIn =
                new BufferedReader(
                    new InputStreamReader(System.in))
        )  {
            RSA.generateKeyPair("client"+clientNumber);
            
            String idC = "client"+clientNumber;
            String inputLine;
            String nonceC = "987"+clientNumber;

            //message 1
            String msg1 = idC;
            out.println(msg1);

            //message 2
            inputLine = in.readLine();
            String msg2Decrypted = RSA.decryptPub(inputLine, idC);
            String nonceS = msg2Decrypted.substring(0,4);
            String idS = msg2Decrypted.substring(4);
            
            
            
            //message 3
            String msg3Encrypted = RSA.encryptPub(nonceS+nonceC, idS);
            out.println(msg3Encrypted);

            //message 4
            inputLine = in.readLine();
            String msg4Decrypted = RSA.decryptPub(inputLine, idC);
            String msg4[] = msg4Decrypted.split("&");
            String keyCS = msg4[1];
            String nonceCRecieved = msg4[0];
            
            if(!nonceCRecieved.equals(nonceC)){
                System.out.println("Nonce C doesn't match, can't authentication Server");
                System.exit(0);
            }
            
            Long sentTime = Long.parseLong(msg4[2]);

            Calendar calendar = Calendar.getInstance();
            long currentTime = calendar.getTimeInMillis();

            if(currentTime - sentTime > 2000){
                System.out.println("Message timeout");
                System.exit(1);
            }
            
            System.out.println(keyCS);
            
            SwingUtilities.invokeLater(() -> new LoginWindow(echoSocket, keyCS, idC));
            
            while (true);
//            String msg = "client&login&goat#bulls";
//            String encMsg = AES.encrypt(msg, keyCS);
//            out.println(encMsg+"&"+calendar.getTimeInMillis());
//            
//            inputLine = in.readLine();
//            if(inputLine.equals("true")){
//                encMsg = AES.encrypt("client&withdrawl&1500000", keyCS);
//                out.println(encMsg + "&" + calendar.getTimeInMillis());
//                inputLine = in.readLine();
//                System.out.println("new balance: " + AES.decrypt(inputLine, keyCS));
//            }
        
            
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }   
}
