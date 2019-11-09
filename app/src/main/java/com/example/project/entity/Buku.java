package com.example.project.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Buku {
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
    @SerializedName("pdf_icon")
    @Expose
    private String pdfIcon;
    @SerializedName("pdf_url")
    @Expose
    private String pdfUrl;
    @SerializedName("peringkat")
    @Expose
    private String peringkat;
    @SerializedName("kategori")
    @Expose
    private String kategori;

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

    public String getPdfIcon() {
        return pdfIcon;
    }

    public void setPdfIcon(String pdfIcon) {
        this.pdfIcon = pdfIcon;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
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
}
