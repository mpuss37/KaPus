package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.swing.*;
import java.io.*;

class convertFile extends Anggota {

    private byte[] fileTxt;

    public byte[] getFileTxt() {
        return fileTxt;
    }

    public void setFileTxt(byte[] fileTxt) {
        this.fileTxt = fileTxt;
    }

    void menuFile() {
        setExit(false);
        setValid(false);
        while (!isExit()) {
            System.out.println("=== Simpan File ===");
            System.out.println("1. txt");
            System.out.println("2. pdf");
            System.out.println("0. Keluar");
            while (!isValid()) {
                System.out.print("Pilih menu : ");
                try {
                    setValid(true);
                    setPilihan(Integer.parseInt(getScanner().nextLine()));
                    switch (getPilihan()) {
                        case 1:
                            ambilBukuTxt();
                            break;
                        case 2:
                            ambilBukuPdf();
                            break;
                        case 0:
                            setExit(true);
                            break;
                        default:
                            System.out.println("Pilihan tidak valid.");
                            break;
                    }
                } catch (NumberFormatException e) {

                }
            }
            setValid(false);
        }
    }


    byte[] pathFile(String filepath) {
        try {
            String text = convertPdfTxt(filepath);
            fileTxt = text.getBytes();
//            String outputtext = changeNameExtensionTxt(filepath);
//            System.out.println("extension : " + changeNamePath(outputtext));
//            System.out.println("path : " + outputtext);
        } catch (Error e) {
            System.out.println(e);
        }
        return fileTxt;
    }

    String getPathFile(String path) {
        JFileChooser fileChooser = new JFileChooser();
        File initialDirectory = new File(System.getProperty("user.home"));
        fileChooser.setCurrentDirectory(initialDirectory);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        try {
            if (result == JFileChooser.APPROVE_OPTION) {
                setSelectedFile(fileChooser.getSelectedFile());
                path = getSelectedFile().getPath();
            } else {
                System.out.println("Tidak ada file yang dipilih.");
            }
        } catch (Error e) {
            System.out.println("file tidak ada");
        }
        return path;
    }

    private String convertPdfTxt(String file) {
        try {
            PDDocument pdDocument = PDDocument.load(new File(file));
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(pdDocument);
            pdDocument.close();
            return text;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String changeNameExtensionTxt(String path) {
        String fileNameWithoutExtension = path.substring(0, path.lastIndexOf("."));
        path = (fileNameWithoutExtension + ".txt");
        return path;
    }

    private String changeNamePath(String path) {
        String pathFile = path;
        File file = new File(pathFile);
        path = file.getName();
        return path;
    }
}