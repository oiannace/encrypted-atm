package pkg817_project_sockets;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.Calendar;

/**
 *
 * @author oiannace
 */
public class ServerThread extends Thread {
    private static final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    Object lock = new Object();
    private Socket socket = null;

    public ServerThread(Socket socket, BlockingQueue<String> messageQueue){
        super("KDCServerThread");
        this.socket = socket;
    }
    public void run() {
 
        try ( 
            PrintWriter out =
                new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
        ) {
         
            String inputLine, outputLine;
            String nonceS = "1010";
            String idS = "server";
            String sharedPassword = "ThisIsASharedKey";
            
            //message 1     
            inputLine = in.readLine();

            String idC = inputLine;
            
            
            //message 2
            String msg1Encrypted = RSA.encryptPub(nonceS+idS,idC);
            out.println(msg1Encrypted);

            //message 3
            inputLine = in.readLine();
            String msg3Decrypted = RSA.decryptPub(inputLine, idS);
            String nonceSRecieved = msg3Decrypted.substring(0,4);
            String nonceC = msg3Decrypted.substring(4);
            
            if(!nonceSRecieved.equals(nonceS)){
                System.out.println("Nonce S doesn't match, can't authentication Client");
                System.exit(0);
            }

            //message 4
            Calendar calendar = Calendar.getInstance();
            long currentTime = calendar.getTimeInMillis();
            String msg4Encrypted = RSA.encryptPub(nonceC+"&"+sharedPassword+"&"+currentTime, idC);
            out.println(msg4Encrypted);

           String decryptedMsg, encryptedMsg;
           String[] msg, decryptedList, userpass = null;
           String idCMsg;
           Calendar calendar1;
           long currentTime1;
           Long sentTime;
           String action;
           Double amount;
           boolean loginSuccess;
           double currBalance;
           
           BankSystem conn1;// = new BankSystem();
            
           
            while(true){
                if(in.ready()){
                    String input = in.readLine();
                    
                    System.out.println("Encrypted message from client: " + input);
                    
                    msg = input.split("&");
                    sentTime = Long.parseLong(msg[1]);

                    
                    
                    calendar1 = Calendar.getInstance();
                    currentTime1 = calendar.getTimeInMillis();

                    if(currentTime - sentTime > 2000){
                        System.out.println("Message timeout");
                        System.exit(1);
                    }

                    decryptedMsg = AES.decrypt(msg[0], sharedPassword);
                    
                    System.out.println("Decrypted message from client: " + decryptedMsg + "\n");
                    
                    decryptedList = decryptedMsg.split("&");
                    idCMsg = decryptedList[0];
                    if(!idCMsg.equals(idC)){
                        System.out.println("Client ID incorrect");
                        System.exit(1);
                    }
                    
                    action = decryptedList[1];
                    
                    if(action.equals("withdraw")){
                        conn1 = new BankSystem();
                        amount = Double.parseDouble(decryptedList[2]);
                        currBalance = conn1.withdraw(userpass[0], amount);
                        encryptedMsg = AES.encrypt(Double.toString(currBalance), sharedPassword);
                        out.println(encryptedMsg);
                        conn1.closeConnection();
                    }
                    else if(action.equals("deposit")){
                        conn1 = new BankSystem();
                        amount = Double.parseDouble(decryptedList[2]);
                        currBalance = conn1.deposit(userpass[0], amount);
                        encryptedMsg = AES.encrypt(Double.toString(currBalance), sharedPassword);
                        out.println(encryptedMsg);
                        conn1.closeConnection();
                    }
                    else if(action.equals("getBalance")){
                        conn1 = new BankSystem();
                        encryptedMsg = AES.encrypt(Double.toString(conn1.viewBalance(userpass[0])), sharedPassword);
                        out.println(encryptedMsg);
                        conn1.closeConnection();
                    }
                    else if(action.equals("login")){
                        conn1 = new BankSystem();
                        userpass = decryptedList[2].split("#");
                        loginSuccess = conn1.login(userpass[0], userpass[1]);
                        out.println(loginSuccess);
                        conn1.closeConnection();
                    }
                    else{
                        System.out.println("action not recognized");
                    }
                    
                } 
            }
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}