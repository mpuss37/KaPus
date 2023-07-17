//Nama Repo Read-Shares
//desc ada di dir home/mpuss/catatan/descreadshares.txt
package org.example;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final String URL = "jdbc:mysql://localhost:3306/perpustakaan";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    private boolean exit, valid;

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    private static Connection connection;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    private Scanner scanner = new Scanner(System.in);
    private int pilihan;

    public int getPilihan() {
        return pilihan;
    }

    public void setPilihan(int pilihan) {
        this.pilihan = pilihan;
    }

    public Connection getConnection() {
        return connection;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    void menuUtama() {
        exit = false;
        Session session = new Session();
        valid = false;
        while (!exit) {
            System.out.println("=== MENU UTAMA ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Keluar");
            while (!valid) {
                System.out.print("Pilih menu : ");
                try {
                    pilihan = Integer.parseInt(scanner.nextLine());
                    valid = true;
                    switch (pilihan) {
                        case 1:
                            session.menuLogin();
                            break;
                        case 2:
                            session.menuRegister();
                            break;
                        case 0:
                            exit = true;
                            break;
                        default:
                            System.out.println("Pilihan tidak valid.");
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input yang dimasukkan bukan angka. Coba lagi.");
                }
            }
            valid = false;
            System.out.println();
        }
        scanner.close();
        closeConnection();
        System.out.println("Terima kasih. Program selesai.");
    }

    void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Tidak dapat menutup koneksi: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Main main = new Main();
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Gagal saat menghubungkan ke database: " + e.getMessage());
        }
        main.menuUtama();
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
