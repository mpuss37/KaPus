package org.example;

import java.io.IOException;
import java.sql.SQLException;

public class Session extends Main {
    private String password, riwayat;
    private int telepon;

    private boolean statusSessionActive = false;

    void menuRegister() {
        Anggota anggota = new Anggota();
        System.out.println("Menu Register User");
        anggota.insertAnggota();
    }

    void menuLogin() {
        Anggota anggota = new Anggota();
//        anggota.setNama("sengkok"); login jadi admin
        System.out.print("Masukkan Nama Anda : ");
        anggota.setNama(getScanner().nextLine());
        System.out.print("Masukkan Password Anda : ");
//        password = "1"; login jadi admin
        password = getScanner().nextLine();
        validateMenu(anggota.getNama(), password);
    }

    void validateMenu(String nama, String password) {
        Buku buku = new Buku();
        Anggota anggota = new Anggota();
        setQuery("SELECT * FROM anggota where nama = ? and password = ?");
        try {
            buku.setStatement(getConnection().prepareStatement(getQuery()));
            buku.getStatement().setString(1, nama);
            buku.getStatement().setString(2, password);
            buku.setResultSet(buku.getStatement().executeQuery());
            if (buku.getResultSet().next()) {
                anggota.setJabatan(buku.getResultSet().getString("jabatan"));
                if (anggota.getJabatan().equals("admin")) {
                    //petinggi nih boss
                    do {
                        nama = buku.getResultSet().getString("nama");
                        anggota.setJabatan(buku.getResultSet().getString("jabatan"));
                        tier(nama, anggota.getJabatan());
                        buku.menuBuku(anggota.getJabatan());
                    } while (buku.getResultSet().next());
                } else if (anggota.getJabatan().equals("user")) {
                    tier(nama, anggota.getJabatan());
                    buku.menuBuku(anggota.getJabatan());
                }
            } else {
                System.out.println("data salah !!!");
            }
        } catch (SQLException e) {
            System.err.println("Gagal saat menghubungkan ke database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void tier(String nama, String jabatan) {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("Berhasil Login " + nama + " dengan Tier ' " + jabatan + " '");
        ;
    }

}
