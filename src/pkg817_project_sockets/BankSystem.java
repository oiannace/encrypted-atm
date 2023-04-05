/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pkg817_project_sockets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author yuvalamir
 */
public class BankSystem {
    
        private Connection conn;
    
        public BankSystem(){
            conn = connect();
        }
        public void closeConnection(){
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
        private Connection connect(){
            Connection c = null;   
            String url = "jdbc:sqlite:bank.db";

                try {       
                    Class.forName("org.sqlite.JDBC");       
                    c = DriverManager.getConnection(url);
                    } catch (Exception e){      
                    System.err.println("Problem Encountered" + e.getMessage());     
                    }                 
            return c;
        }
        
    public double viewBalance(String username) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM account WHERE username = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double balance = rs.getDouble("balance");
                    return balance;
                } else {
                    System.out.println("Could not retrieve balance for user " + username);
                }
                
            }
        } catch (SQLException e) {
            System.err.println("Problem encountered: " + e.getMessage());
        }
        
        return 0.0;
    }
    
    public double deposit(String username, double amount) {
        double balance;
        try {
            Statement pragmaStmt = conn.createStatement();
            PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM account WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");

                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE account SET balance = ? WHERE username = ?");
                    updateStmt.setDouble(1, balance + amount);
                    updateStmt.setString(2, username);
                    updateStmt.executeUpdate();
                    //System.out.println("\n$" + amount + " has been deposited into your account.");
                    //System.out.println("\nNew Balance: $" + balance);
                    //conn.commit(); // commit the transaction

                    // Log the transaction
                    File file = new File("transactions.txt");
                    boolean fileExists = false;
                    boolean canWriteToFile = false;
                    try {
                        if (file.exists()) {
                            fileExists = true;
                            if (file.canWrite()) {
                                canWriteToFile = true;
                                FileWriter fw = new FileWriter(file, true);
                                BufferedWriter bw = new BufferedWriter(fw);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String dateString = dateFormat.format(new Date());
                                String log = username + ", Deposit, " + dateString + ", " + amount + ", " + (balance - amount) + "\n";
                                bw.write(log);
                                bw.close();
                                //System.out.println("Transaction logged successfully.");
                            } else {
                                System.out.println("Cannot write to transactions.txt");
                            }
                        } else {
                            fileExists = file.createNewFile();
                            if (fileExists) {
                                //System.out.println("transactions.txt created.");
                                canWriteToFile = file.canWrite();
                                if (canWriteToFile) {
                                    FileWriter fw = new FileWriter(file, true);
                                    BufferedWriter bw = new BufferedWriter(fw);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String dateString = dateFormat.format(new Date());
                                    String log = username + ", Withdrawal, " + dateString + ", " + amount + ", " + (balance - amount) + "\n";
                                    bw.write(log);
                                    bw.close();
                                    //System.out.println("Transaction logged successfully.");
                                } else {
                                    System.out.println("Cannot write to transactions.txt");
                                }
                            } else {
                                System.out.println("Unable to create transactions.txt");
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Error writing to transactions.txt: " + e.getMessage());
                    }
                
            } else {
                System.out.println("Could not retrieve balance for user " + username);
            }
            rs.close();
            stmt.close();
            pragmaStmt.close();
            //conn.close();
        } catch (SQLException e) {
            System.err.println("Problem encountered: " + e.getMessage());
        }
        return viewBalance(username);
    }
    
    public double withdraw(String username, double amount) {
        double balance;
        try {
            Statement pragmaStmt = conn.createStatement();
            PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM account WHERE username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
                if (balance < amount) {
                    System.out.println("\nInsufficient funds!");
                } else {
                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE account SET balance = ? WHERE username = ?");
                    updateStmt.setDouble(1, balance - amount);
                    updateStmt.setString(2, username);
                    updateStmt.executeUpdate();
                    //System.out.println("\n$" + amount + " has been withdrawn from your account.");
                    //System.out.println("\nNew Balance: $" + balance);
                    //conn.commit(); // commit the transaction

                    // Log the transaction
                    File file = new File("transactions.txt");
                    boolean fileExists = false;
                    boolean canWriteToFile = false;
                    try {
                        if (file.exists()) {
                            fileExists = true;
                            if (file.canWrite()) {
                                canWriteToFile = true;
                                FileWriter fw = new FileWriter(file, true);
                                BufferedWriter bw = new BufferedWriter(fw);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String dateString = dateFormat.format(new Date());
                                String log = username + ", Withdrawal, " + dateString + ", " + amount + ", " + (balance - amount) + "\n";
                                bw.write(log);
                                bw.close();
                                //System.out.println("Transaction logged successfully.");
                            } else {
                                System.out.println("Cannot write to transactions.txt");
                            }
                        } else {
                            fileExists = file.createNewFile();
                            if (fileExists) {
                                //System.out.println("transactions.txt created.");
                                canWriteToFile = file.canWrite();
                                if (canWriteToFile) {
                                    FileWriter fw = new FileWriter(file, true);
                                    BufferedWriter bw = new BufferedWriter(fw);
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String dateString = dateFormat.format(new Date());
                                    String log = username + ", Withdrawal, " + dateString + ", " + amount + ", " + (balance - amount) + "\n";
                                    bw.write(log);
                                    bw.close();
                                    //System.out.println("Transaction logged successfully.");
                                } else {
                                    System.out.println("Cannot write to transactions.txt");
                                }
                            } else {
                                System.out.println("Unable to create transactions.txt");
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Error writing to transactions.txt: " + e.getMessage());
                    }
                }
            } else {
                System.out.println("Could not retrieve balance for user " + username);
            }
            rs.close();
            stmt.close();
            pragmaStmt.close();
            //conn.close();
        } catch (SQLException e) {
            System.err.println("Problem encountered: " + e.getMessage());
        }
        return viewBalance(username);
    }
     
    public void viewTransactions(String username) {
        try {
            File file = new File("/Users/yuvalamir/Desktop/Ryerson/4thYear/Winter2023/COE 817/FinalProject/BankSystem/src/banksystem/transactions.txt");
            if (!file.exists()) {
                System.out.println("No transactions found.");
                return;
            }
            Scanner scanner = new Scanner(file);
            boolean hasHeader = true;
            System.out.println("Transactions for " + username + ":");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (hasHeader) {
                    hasHeader = false;
                    continue; // skip the header row
                }
                String[] parts = line.split(",");
                String transactionUsername = parts[0];
                String transactionType = parts[1];
                String transactionDate = parts[2];
                double transactionAmount = Double.parseDouble(parts[3]);
                double transactionBalance = Double.parseDouble(parts[4]);
                if (transactionUsername.equals(username)) {
                    System.out.println(transactionType + " on " + transactionDate + " | Amount: $" + transactionAmount + " | New Balance: $" + transactionBalance);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("No transactions found.");
        }
    }
     
        public boolean login(String username, String password){
            
            try {
                    //Connection connection = connect();
                    Statement statement = conn.createStatement();
                    
                        // Check if the user is a client
                        String query = "SELECT * FROM account WHERE username='" + username + "' AND password='" + password + "'";
                        ResultSet rs = statement.executeQuery(query);
                        if (rs.next()) {
                            // If the login is successful, call the client main menu
                            //conn.close();
                            return true;
                        } else {
                            System.out.println("Invalid login credentials.");
                            
                        }

                    // Close the statement and connection objects
                    statement.close();
                    //conn.close();
                } catch (SQLException e) {
                    System.err.println("Problem encountered: " + e.getMessage());
                }    
            return false;
        }


    
}
