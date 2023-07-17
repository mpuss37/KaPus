package org.example;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Anggota extends Buku {

    Buku buku;

    public Anggota() {
        buku = new Buku();
    }

    private String nama, password, email, jabatan;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    void menuAnggota() {
        setExit(false);
        while (!isExit()) {
            System.out.println("-----------------------------------");
            System.out.println("=== MENU UTAMA ===");
            System.out.println("1. Insert Angota");
            System.out.println("2. Tampil Anggota");
            System.out.println("3. Hapus Anggota");
            System.out.println("4. Update Anggota");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu : ");
            setPilihan(getScanner().nextInt());
            getScanner().nextLine();
            switch (getPilihan()) {
                case 1:
                    insertAnggota();
                    break;
                case 2:
                    tampilAnggota();
                    break;
                case 3:
                    hapusAnggota();
                    break;
                case 4:
                    break;
                case 0:
                    setExit(true);
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
                    break;
            }
        }
    }

    void insertAnggota() {
        try {
            System.out.print("Masukkan nama : ");
            nama = getScanner().nextLine();
            setQuery("SELECT COUNT(*) FROM anggota WHERE nama = ?");
            setStatement(getConnection().prepareStatement(getQuery()));
            getStatement().setString(1, nama);
            ResultSet resultSet = getStatement().executeQuery();
            resultSet.next();
            int jumlah = resultSet.getInt(1);
            if (jumlah > 0) {
                System.out.println("nama : " + nama);
                System.out.println("Data sudah dipakai orang lain :)");
            } else {
                password = hashPassword("Masukkan password");
//            System.out.print("Masukkan email : ");
//            email = getScanner().nextLine();
                email = "user";
                jabatan = "user";
                setQuery("INSERT INTO anggota (nama, password, email, jabatan) VALUES (?, ?, ?, ?)");
                buku.setStatement(getConnection().prepareStatement(getQuery()));
                buku.getStatement().setString(1, nama);
                buku.getStatement().setString(2, password);
                buku.getStatement().setString(3, email);
                buku.getStatement().setString(4, jabatan);
                int rowsInserted = buku.getStatement().executeUpdate();
                if (rowsInserted > 0) {
                    System.out.print("\033[H\033[2J");
                    System.out.println("-----------------------------------");
                    System.out.println("Data berhasil diinsert.");
                    System.out.println("Nama : " + nama);
                    System.out.println("Email : " + email);
//                System.out.println("Jabatan : " + jabatan);
                    System.out.println("-----------------------------------");
                } else {
                    System.out.println("Gagal memasukkan data.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
        }
    }

    String hashPassword(String sout) {
        System.out.print(sout + " : ");
        password = getScanner().nextLine();
        return password;
    }

    void tampilAnggota() {
        Buku buku = new Buku();
        System.out.println("=== TAMPIL ANGGOTA ===");
        try {
            setQuery("SELECT * FROM anggota");
            buku.setStatement(getConnection().prepareStatement(getQuery()));

            ResultSet resultSet = buku.getStatement().executeQuery();
            if (resultSet.next()) {
                do {
                    buku.setId(resultSet.getInt("id"));
                    nama = resultSet.getString("nama");
                    email = resultSet.getString("email");
                    jabatan = resultSet.getString("jabatan");
                    System.out.println("-----------------------------------");
                    System.out.println("ID : " + buku.getId());
                    System.out.println("Nama : " + nama);
                    System.out.println("email : " + email);
                    System.out.println("jabatan : " + jabatan);
                    System.out.println("-----------------------------------");
                } while (resultSet.next());
            } else {
                System.out.println("data dengan id 1 tidak ada");
            }
        } catch (SQLException e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
            System.out.println("-----------------------------------");
        }
    }

    void hapusAnggota() {
        Buku buku = new Buku();
        try {
            setQuery("DELETE FROM anggota WHERE id = ?");
            PreparedStatement statement = getConnection().prepareStatement(getQuery());
            System.out.print("Masukkan Id-Anggota Yang Ingin Dihapus : ");
            buku.setId(getScanner().nextInt());
            statement.setInt(1, buku.getId());

            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Data anggota dengan ID " + buku.getId() + " berhasil dihapus.");
                System.out.println("-----------------------------------");
            } else {
                System.out.println("Data anggota dengan ID " + buku.getId() + " tidak ditemukan.");
                System.out.println("-----------------------------------");
            }
        } catch (SQLException e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
        }
    }
}