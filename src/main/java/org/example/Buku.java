package org.example;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class Buku extends Session {

    StringBuilder notFoundIds;
    private PreparedStatement statement;
    private ResultSet resultSet;
    private String judul, penulis, kategori, path;
    private int id, tahunterbit, jumlahhalaman;

    private byte[] filePdf;

    private File selectedFile;

    public byte[] getFilePdf() {
        return filePdf;
    }

    public void setFilePdf(byte[] filePdf) {
        this.filePdf = filePdf;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PreparedStatement getStatement() {
        return statement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public void setStatement(PreparedStatement statement) {
        this.statement = statement;
    }

    void menuBuku(String jabatan) {
        convertFile convertFile = new convertFile();
        if (jabatan.equals("admin")) {
            setExit(false);
            setValid(false);
            while (!isExit()) {
                while (!isValid()) {
                    System.out.println("=== MENU UTAMA ADMIN ===");
                    System.out.println("1. Insert Buku");
                    System.out.println("2. Tampil Buku");
//                    System.out.println("3. Hapus Buku");
                    System.out.println("3. Ambil Buku");
                    System.out.println("0. Keluar");
                    System.out.print("Pilih menu : ");
                    try {
                        setPilihan(Integer.parseInt(getScanner().nextLine()));
                        setValid(true);
                        switch (getPilihan()) {
                            case 1:
                                insertBuku();
                                break;
                            case 2:
                                tampilBuku();
                                break;
//                            case 3:
//                                hapusBuku();
//                                break;
                            case 3:
                                convertFile.menuFile();
                                break;
                            case 0:
                                setExit(true);
                                break;
                            default:
                                System.out.println("Pilihan tidak valid.");
                                break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Input yang dimasukkan bukan angka. Coba lagi.");
                    }
                }
                setValid(false);
            }
        } else {
            setExit(false);
            setValid(false);
            while (!isExit()) {
                System.out.println("=== MENU UTAMA USER ===");
                System.out.println("1. Tampil Buku");
                System.out.println("0. Keluar");
                while (!isValid()) {
                    try {
                        setValid(true);
                        System.out.print("Pilih menu : ");
                        setPilihan(Integer.parseInt(getScanner().nextLine()));
                        switch (getPilihan()) {
                            case 1:
                                tampilBuku();
                                break;
                            case 0:
                                setExit(true);
                                break;
                            default:
                                System.out.println("Pilihan tidak valid.");
                                break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Input yang dimasukkan bukan angka. Coba lagi.");
                    }
                }
            }
            setValid(false);
        }
    }

    void insertBuku() {
        JFileChooser fileChooser = new JFileChooser();
        convertFile convertFile = new convertFile();
        File initialDirectory = new File(System.getProperty("user.home"));
        fileChooser.setCurrentDirectory(initialDirectory);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            System.out.println("=== INSERT BUKU ===");
            try {
                setQuery("SELECT COUNT(*) FROM buku WHERE judul = ?");
                judul = selectedFile.getName();
                setStatement(getConnection().prepareStatement(getQuery()));
                getStatement().setString(1, judul);
                ResultSet resultSet = getStatement().executeQuery();
                resultSet.next();
                int jumlah = resultSet.getInt(1);
                if (jumlah > 0) {
                    System.out.println("Buku dengan judul : " + judul);
                    System.out.println("Data sudah !!!");
                } else {
                    penulis = "isi_sendiri";
                    tahunterbit = 0;
                    kategori = "isi_sendiri";
                    jumlahhalaman = 0;
                    String judultxt = selectedFile.getPath();
                    convertFile.pathFile(judultxt);
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    filePdf = new byte[(int) selectedFile.length()];
                    fileInputStream.read(filePdf);
                    fileInputStream.close();
                    System.out.println("Masukkan penulis buku: " + penulis);
//                penulis = getScanner().nextLine();

                    System.out.println("Masukkan tahun terbit buku: " + tahunterbit);
//                tahunterbit = getScanner().nextInt();
//                getScanner().nextLine(); // Membersihkan karakter baru (\n) dalam buffer

                    System.out.println("Masukkan kategori buku: " + kategori);
//                kategori = getScanner().nextLine();

                    System.out.println("Masukkan jumlah halaman buku: " + jumlahhalaman);
//                jumlahhalaman = getScanner().nextInt();
//                getScanner().nextLine();
                    setQuery("INSERT INTO buku (file_pdf, file_txt, judul, penulis, tahun_terbit, kategori, jumlah_halaman) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    statement = getConnection().prepareStatement(getQuery());
                    statement.setBytes(1, filePdf);
                    statement.setBytes(2, convertFile.getFileTxt());
                    statement.setString(3, judul);
                    statement.setString(4, penulis);
                    statement.setInt(5, tahunterbit);
                    statement.setString(6, kategori);
                    statement.setInt(7, jumlahhalaman);

                    int rowsInserted = statement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Data berhasil disimpan.");
                        System.out.println("Judul: " + judul);
                        System.out.println("Penulis: " + penulis);
                        System.out.println("Tahun Terbit: " + tahunterbit);
                        System.out.println("Kategori: " + kategori);
                        System.out.println("Jumlah Halaman: " + jumlahhalaman);
                        System.out.println("-----------------------------------");

                    } else {
                        System.out.println("Gagal memasukkan data.");
                    }
                }
            } catch (IOException | SQLException e) {
                System.err.println("Terjadi kesalahan: " + e.getMessage());
            }
        } else {
            System.out.println("Tidak ada file yang dipilih.");
        }
    }

    void tampilBuku() {
        System.out.println("=== TAMPIL BUKU ===");
        try {
            setQuery("SELECT * FROM buku");
            statement = getConnection().prepareStatement(getQuery());

            resultSet = statement.executeQuery();
            id = 1;
            if (resultSet.next()) {
                do {
                    id = resultSet.getInt("id");
                    judul = resultSet.getString("judul");
                    penulis = resultSet.getString("penulis");
                    tahunterbit = resultSet.getInt("tahun_terbit");
                    kategori = resultSet.getString("kategori");
                    jumlahhalaman = resultSet.getInt("jumlah_halaman");

                    System.out.println("-----------------------------------");
                    System.out.println("ID: " + id);
                    System.out.println("Judul: " + judul);
                    System.out.println("Penulis: " + penulis);
                    System.out.println("Tahun Terbit: " + tahunterbit);
                    System.out.println("Kategori: " + kategori);
                    System.out.println("Jumlah Halaman: " + jumlahhalaman);
                } while (resultSet.next());
            } else {
                System.out.println("Data kosong, Data dengan id '" + id + "' tidak ada !!!");
                System.out.println("-----------------------------------");
            }
        } catch (SQLException e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
        }
    }

    void ambilBukuTxt() {
        convertFile convertFile = new convertFile();
        try {
            setValid(false);
            System.out.print("Masukkan id buku : ");
            while (!isValid()) {
                setValid(true);
                id = Integer.parseInt(getScanner().nextLine());
                setQuery("SELECT * FROM buku where id = ?");
                statement = getConnection().prepareStatement(getQuery());
                statement.setInt(1, id);
                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    judul = resultSet.getString("judul");
                    System.out.println(judul);
                    convertFile.setFileTxt(resultSet.getBytes("file_txt"));
                    System.out.println("* tentukan direktori dimana akan disimpan *");
                    path = (convertFile.getPathFile(path));
                    if (path != null) {
                        setPath(path + "/" + convertFile.changeNameExtensionTxt(judul));
                        FileOutputStream fileOutputStream = new FileOutputStream(getPath());
                        fileOutputStream.write(convertFile.getFileTxt());
                        fileOutputStream.close();
                    }
                } else {
                    System.out.println("File tidak ditemukan dengan Id : " + id);
                }
            }
        } catch (SQLException | NumberFormatException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    void ambilBukuPdf() {
        convertFile convertFile = new convertFile();
        try {
            setValid(false);
            System.out.print("Masukkan id buku : ");
            while (!isValid()) {
                setValid(true);
                id = Integer.parseInt(getScanner().nextLine());
                setQuery("SELECT * FROM buku where id = ?");
                statement = getConnection().prepareStatement(getQuery());
                statement.setInt(1, id);
                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    judul = resultSet.getString("judul");
                    System.out.println("Judul Buku : "+judul);
                    filePdf = resultSet.getBytes("file_pdf");
                    path = (convertFile.getPathFile(path));
                    if (path != null) {
                        setPath(path + "/" + judul);
                        FileOutputStream fileOutputStream = new FileOutputStream(getPath());
                        fileOutputStream.write(filePdf);
                        fileOutputStream.close();
                        System.out.println("Berhasil Disimpan di : "+path);
                    }
                } else {
                    System.out.println("File tidak ditemukan dengan Id : " + id);
                }
            }
        } catch (SQLException | NumberFormatException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    void hapusBuku() {
        setValid(false);
        setExit(false);
        while (!isExit()) {
            System.out.println("1. Hapus berdasarkan ID\n2. Hapus berdasarkan Judul\n0. Keluar");
            while (!isValid()) {
                System.out.print("Masukkan pilihan anda : ");
                try {
                    setPilihan(Integer.parseInt(getScanner().nextLine()));
                    setValid(true);
                    switch (getPilihan()) {
                        case 1:
                            setQuery("SELECT * FROM buku WHERE id = ?");
                            statement = getConnection().prepareStatement(getQuery());
                            System.out.println("Format Penulisan : 1 2 3 4 (dipisahkan dengan spasi)");
                            System.out.print("Masukkan Id-Buku Yang Ingin Dihapus : ");
                            String idBukuInput = getScanner().nextLine();
                            String[] idBukuArray = idBukuInput.split(" ");
                            System.out.println("1. Hapus Satu\"\n2.Hapus Semua");
                            System.out.print("Masukkan pilihan anda : ");
                            setPilihan(getScanner().nextInt());
                            getScanner().nextLine();
                            switch (getPilihan()) {
                                case 1:
                                    for (String idBukuStr : idBukuArray) {
                                        int idBuku = Integer.parseInt(idBukuStr);
                                        statement.setInt(1, idBuku);
                                        resultSet = statement.executeQuery();
                                        if (resultSet.next()) {
                                            id = resultSet.getInt("id");
                                            judul = resultSet.getString("judul");
                                            System.out.println("Data buku dengan ID '" + id + "' Dengan Judul '" + judul + "' berhasil ditemukan.");
                                            System.out.print("apakah anda yakin ingin menghapusnya [y/n] ");
                                            String yakindantidak = getScanner().nextLine();
                                            if (yakindantidak.equals("y")) {
                                                setQuery("DELETE FROM buku WHERE id = ?");
                                                PreparedStatement deleteStatement = getConnection().prepareStatement(getQuery());
                                                deleteStatement.setInt(1, idBuku);
                                                int rowsDeleted = deleteStatement.executeUpdate();
                                                if (rowsDeleted > 0) {
                                                    System.out.println("Buku dengan ID " + idBuku + " berhasil dihapus.");
                                                } else {
                                                    System.out.println("Gagal menghapus buku dengan ID " + idBuku + ".");
                                                }
                                            } else if (yakindantidak.equals("n")) {
                                                System.out.println("Gagal menghapus buku dengan ID " + id);
                                            } else {
                                                System.out.println("Gagal menghapus buku dengan ID " + id);
                                            }
                                        } else {
                                            if (notFoundIds.length() > 0) {
                                                notFoundIds.append(",");
                                            }
                                            notFoundIds.append(idBuku);
                                        }
                                    }

                                    if (notFoundIds.length() > 0) {
                                        System.out.println("Buku dengan ID " + notFoundIds.toString() + " tidak ditemukan.");
                                    }
                                    break;
                                case 2:
                                    for (String idBukuStr : idBukuArray) {
                                        id = Integer.parseInt(idBukuStr);
                                        statement.setInt(1, id);
                                        resultSet = statement.executeQuery();
                                        if (resultSet.next()) {
                                            id = resultSet.getInt("id");
                                            judul = resultSet.getString("judul");
                                            System.out.println("Data buku dengan ID '" + id + "' Dengan Judul '" + judul + "' berhasil ditemukan.");
                                            System.out.print("Apakah Anda yakin ingin menghapusnya [y/n] ");
                                            String yakindantidak = getScanner().nextLine();
                                            if (yakindantidak.equals("y")) {
                                                setQuery("DELETE FROM buku WHERE id = ?");
                                                PreparedStatement deleteStatement = getConnection().prepareStatement(getQuery());
                                                deleteStatement.setInt(1, id);
                                                int rowsDeleted = deleteStatement.executeUpdate();
                                                if (rowsDeleted > 0) {
                                                    System.out.println("Buku dengan ID " + id + " berhasil dihapus.");
                                                } else {
                                                    System.out.println("Gagal menghapus buku dengan ID " + id + ".");
                                                }
                                            } else if (yakindantidak.equals("n")) {
                                                System.out.println("Gagal menghapus buku dengan ID " + id);
                                            } else {
                                                System.out.println("Gagal menghapus buku dengan ID " + id);
                                            }
                                        } else {
                                            if (notFoundIds.length() > 0) {
                                                notFoundIds.append(",");
                                            }
                                            notFoundIds.append(id);
                                        }
                                    }
                                    if (notFoundIds.length() > 0) {
                                        System.out.println("Buku dengan ID " + notFoundIds.toString() + " tidak ditemukan.");
                                    }
                                    break;
                                case 0:
                                    setExit(true);
                                default:
                                    break;
                            }
                            break;
                        case 0:
                            setExit(true);
                        default:
                            break;
                    }
                } catch (SQLException | NumberFormatException e) {
                    System.err.println("Terjadi kesalahan: " + e.getMessage());
                }
            }
            setValid(false);
        }
    }

    void updateBuku() {
        try {
            setQuery("SELECT * FROM buku WHERE id = ?");
            statement = getConnection().prepareStatement(getQuery());
            System.out.println("Format Penulisan : 1 2 3 4 (dipisahkan dengan spasi)");
            System.out.print("Masukkan Id-Buku Yang Ingin Dihapus : ");
            String idBukuInput = getScanner().nextLine();
            String[] idBukuArray = idBukuInput.split(" ");
            for (String idBukuStr : idBukuArray) {
                id = Integer.parseInt(idBukuStr);
                statement.setInt(1, id);
                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    System.out.println("Data buku dengan ID " + id + " berhasil ditemukan.");
                    System.out.print("apakah anda yakin ingin menghapusnya [y/n] ");
                    String yakindantidak = getScanner().nextLine();
                    if (yakindantidak.equals("y")) {
                        String deleteBuku = "DELETE FROM buku WHERE id = ?";
                        statement = getConnection().prepareStatement(deleteBuku);
                        statement.setInt(1, id);
                        int rowsDeleted = statement.executeUpdate();
                        if (rowsDeleted > 0) {
                            System.out.println("Buku dengan ID " + id + " berhasil dihapus.");
                        } else {
                            System.out.println("Gagal menghapus buku dengan ID " + id + ".");
                        }
                    } else if (yakindantidak.equals("n")) {
                        System.out.println("Gagal menghapus buku dengan ID " + id);
                    } else {
                        System.out.println("Gagal menghapus buku dengan ID " + id);
                    }
                } else {
                    System.out.println("Buku dengan ID " + id + " tidak ditemukan.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
        }
//            ResultSet resultSet = statement.executeQuery();
//            String updateBuku = "UPDATE buku set judul = ?, penulis = ? , tahun_terbit = ?, kategori = ?, jumlah_halaman = ? WHERE id = ?";
//            PreparedStatement statement = main.getConnection().prepareStatement(updateBuku);
//            System.out.print("Masukkan Id-Buku Yang Ingin Diubah : ");
//            int idBuku = main.getScanner().nextInt();
    }
}