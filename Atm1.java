package com.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Atm1 {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/atm?useSSL=false";
    public static final String DB_USER = "root"; // replace with your database username
    public static final String DB_PASSWORD = "Root#java03"; // replace with your database password

    public static boolean validate(int inputPin) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE pin = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, inputPin);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static double getBalance(int pin) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT balance FROM users WHERE pin = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, pin);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void updateBalance(int pin, double newBalance) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE users SET balance = ? WHERE pin = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setDouble(1, newBalance);
            stmt.setInt(2, pin);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cashWithdraw(int pin) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the amount to withdraw");
        int amt = sc.nextInt();
        double balance = getBalance(pin);
        if (amt <= balance) {
            updateBalance(pin, balance - amt);
            System.out.println("Cash withdrawn successfully!!!\n");
            System.out.println("Your available balance is " + getBalance(pin) + "\n");
        } else {
            System.out.println("Insufficient balance!");
        }
    }

    public static void balanceInquiry(int pin) {
        System.out.println("Your available balance is " + getBalance(pin) + "\n");
    }

    public static void deposit(int pin) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the amount to deposit");
        int amt = sc.nextInt();
        double balance = getBalance(pin);
        updateBalance(pin, balance + amt);
        System.out.println("Cash deposited successfully!!!\n");
        System.out.println("Your available balance is " + getBalance(pin) + "\n");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter your PIN ");
            int pin = sc.nextInt();
            if (!validate(pin)) {
                System.out.println("!!! Invalid pin !!!");
                continue;
            }
             while (true) {
                System.out.println("\n1.Cash Withdrawal\n2.Balance Inquiry\n3.Deposit\n4.Exit");
                System.out.println("Select the transaction");
                int c = sc.nextInt();
                switch (c) {
                    case 1:
                        cashWithdraw(pin);
                        break;
                    case 2:
                        balanceInquiry(pin);
                        break;
                    case 3:
                        deposit(pin);
                        break;
                    case 4:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid Choice");
                }
            }
        }
    }
}