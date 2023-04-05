package pkg817_project_sockets;
import java.net.*;
import java.io.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
/**
 *
 * @author oiannace
 */
public class MultiServer {
    private static final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    
    public static void main(String[] args) throws IOException {
         
        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }

        RSA.generateKeyPair("server");

        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;
        try ( 
            ServerSocket serverSocket = new ServerSocket(portNumber);
        ) {
            while(listening){
                Socket clientSocket = serverSocket.accept();
                ServerThread thread = new ServerThread(clientSocket, messageQueue);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}