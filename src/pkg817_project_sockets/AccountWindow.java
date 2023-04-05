package pkg817_project_sockets;

import javax.swing.*;
import java.net.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

public class AccountWindow extends JFrame {
    private JLabel balanceLabel;
    private JTextField withdrawField;
    private JTextField depositField;
    private Socket socket;
    private String username;
    private double balance = 0.0;
    private String keyCS;
    private String clientID;
    public AccountWindow(Socket socket, String keyCS, String username, String clientID) {
        this.socket = socket;
        this.username = username;
        this.keyCS = keyCS;
        this.clientID = clientID;
        
        setTitle("Bank Account - " + username);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setSize(400, 200);

        balanceLabel = new JLabel("Balance: $" + balance);
        balanceLabel.setBounds(10, 10, 250, 25);
        add(balanceLabel);
        
        //
        JLabel withdrawLabel = new JLabel("Withdraw:");
        withdrawLabel.setBounds(10, 50, 250, 25);
        add(withdrawLabel);

        withdrawField = new JTextField();
        withdrawField.setBounds(100, 50, 100, 25);
        add(withdrawField);

        //
        JButton withdrawButton = new JButton("Submit");
        withdrawButton.setBounds(210, 50, 80, 25);
        withdrawButton.addActionListener(new WithdrawButtonListener(socket, keyCS, clientID));
        add(withdrawButton);

        //
        JLabel depositLabel = new JLabel("Deposit:");
        depositLabel.setBounds(10, 90, 80, 25);
        add(depositLabel);

        depositField = new JTextField();
        depositField.setBounds(100, 90, 100, 25);
        add(depositField);

        //
        JButton depositButton = new JButton("Submit");
        depositButton.setBounds(210, 90, 80, 25);
        depositButton.addActionListener(new DepositButtonListener(socket, keyCS, clientID));
        add(depositButton);

        setLocationRelativeTo(null);
        setVisible(true);
        
        //
        JButton logoutButton = new JButton("Log out");
        logoutButton.setBounds(210, 130, 80, 25);
        logoutButton.addActionListener(new LogoutButtonListener());
        add(logoutButton);

        setLocationRelativeTo(null);
        setVisible(true);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
            Calendar calendar = Calendar.getInstance();
            long currentTime = calendar.getTimeInMillis();
            try {
                 // Initialize the input/output streams for the socket
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                
                String msg = clientID+"&getBalance&"+username+"# ";
                String encMsg = AES.encrypt(msg, keyCS);
                out.println(encMsg+"&"+calendar.getTimeInMillis());
                
                String inputLine = in.readLine();
                String decryptedMsg = AES.decrypt(inputLine, keyCS);
                updateBalanceLabel(Double.parseDouble(decryptedMsg));
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(AccountWindow.this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            }
        });
    }

    
    private class WithdrawButtonListener implements ActionListener {
        private Socket socket;
        private String keyCS;
        private String clientID;
        public WithdrawButtonListener(Socket socket, String keyCS, String clientID) {
            this.socket = socket;
            this.keyCS = keyCS;
            this.clientID = clientID;
        }
        
        public void actionPerformed(ActionEvent e) {
            Calendar calendar = Calendar.getInstance();
            long currentTime = calendar.getTimeInMillis();
            try {
                 // Initialize the input/output streams for the socket
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                
                String msg = clientID+"&withdraw&"+withdrawField.getText();
                String encMsg = AES.encrypt(msg, keyCS);
                out.println(encMsg+"&"+calendar.getTimeInMillis());
                
                String inputLine = in.readLine();
                String decryptedMsg = AES.decrypt(inputLine, keyCS);
                updateBalanceLabel(Double.parseDouble(decryptedMsg));
                withdrawField.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(AccountWindow.this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    private class DepositButtonListener implements ActionListener {
        private Socket socket;
        private String keyCS;
        private String clientID;
        public DepositButtonListener(Socket socket, String keyCS, String clientID) {
            this.socket = socket;
            this.keyCS = keyCS;
            this.clientID = clientID;
        }
        
        public void actionPerformed(ActionEvent e) {
            Calendar calendar = Calendar.getInstance();
            long currentTime = calendar.getTimeInMillis();
            try {
                 // Initialize the input/output streams for the socket
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                
                String msg = clientID+"&deposit&"+depositField.getText();
                String encMsg = AES.encrypt(msg, keyCS);
                out.println(encMsg+"&"+calendar.getTimeInMillis());
                
                String inputLine = in.readLine();
                String decryptedMsg = AES.decrypt(inputLine, keyCS);
                updateBalanceLabel(Double.parseDouble(decryptedMsg));
                depositField.setText("");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(AccountWindow.this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    private void updateBalanceLabel(double balance) {
        balance = Math.round(balance * 100.0)/100.0;
        balanceLabel.setText("Balance: $" + balance);
    }
   
    private class LogoutButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new LoginWindow(socket, keyCS, clientID);
            AccountWindow.this.dispose();
        }
    } 
    
}

