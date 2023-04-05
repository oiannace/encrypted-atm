package pkg817_project_sockets;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.net.*;
import java.io.*;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Socket socket;

    public LoginWindow(Socket socket, String keyCS, String clientID) {
        this.socket = socket;
        
        setTitle("Bank Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setSize(300, 150);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 10, 80, 25);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(100, 10, 160, 25);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 40, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 40, 160, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 80, 80, 25);
        loginButton.addActionListener(new LoginButtonListener(socket, keyCS, clientID));
        add(loginButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class LoginButtonListener implements ActionListener {
        private Socket socket;
        private String keyCS;
        private String clientID;
        public LoginButtonListener(Socket socket, String keyCS, String clientID) {
            this.socket = socket;
            this.keyCS = keyCS;
            this.clientID = clientID;
        }
        
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            Calendar calendar = Calendar.getInstance();
            long currentTime = calendar.getTimeInMillis();
            try {
                // Initialize the input/output streams for the socket
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                
                String msg = clientID+"&login&"+username+"#"+password;
                String encMsg = AES.encrypt(msg, keyCS);
                out.println(encMsg+"&"+calendar.getTimeInMillis());
                String inputLine = in.readLine();
                if (!inputLine.equals("true")) {
                    JOptionPane.showMessageDialog(LoginWindow.this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    new AccountWindow(socket, keyCS, username, clientID);
                    LoginWindow.this.dispose();
                }
            
                //out.close();
                //in.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}

