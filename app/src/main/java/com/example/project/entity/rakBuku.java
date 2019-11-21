package com.example.project.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class rakBuku {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("pdf_url")
    @Expose
    private String pdfUrl;
    @SerializedName("pdf_icon")
    @Expose
    private String pdfIcon;
    @SerializedName("peringkat")
    @Expose
    private String peringkat;
    @SerializedName("kategori")
    @Expose
    private String kategori;
    @SerializedName("id_user")
    @Expose
    private String id_user;
    @SerializedName("pengunjung")
    @Expose
    private int pengunjung;
    @SerializedName("id_buku")
    @Expose
    private String id_buku;


    public rakBuku(String id, String nama, String deskripsi,
                   String author, String pdfUrl, String pdfIcon,
                   String peringkat, String kategori, String id_user, int pengunjung, String id_buku){
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.author = author;
        this.pdfUrl = pdfUrl;
        this.pdfIcon = pdfIcon;
        this.peringkat = peringkat;
        this.kategori = kategori;
        this.id_user = id_user;
        this.pengunjung = pengunjung;
        this.id_buku = id_buku;
    }

    public String getId_buku() {
        return id_buku;
    }

    public void setId_buku(String id_buku) {
        this.id_buku = id_buku;
    }

    public int getPengunjung() {
        return pengunjung;
    }

    public void setPengunjung(int pengunjung) {
        this.pengunjung = pengunjung;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getPdfIcon() {
        return pdfIcon;
    }

    public void setPdfIcon(String pdfIcon) {
        this.pdfIcon = pdfIcon;
    }

    public String getPeringkat() {
        return peringkat;
    }

    public void setPeringkat(String peringkat) {
        this.peringkat = peringkat;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
