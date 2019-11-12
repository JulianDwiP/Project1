package com.example.project.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class rakBuku {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("Rb_nama")
    @Expose
    private String Rb_nama;
    @SerializedName("Rb_deskripsi")
    @Expose
    private String Rb_deskripsi;
    @SerializedName("Rb_author")
    @Expose
    private String Rb_author;
    @SerializedName("Rb_pdf_url")
    @Expose
    private String Rb_pdfUrl;
    @SerializedName("Rb_pdf_icon")
    @Expose
    private String Rb_pdfIcon;
    @SerializedName("peringkat")
    @Expose
    private String Rb_peringkat;
    @SerializedName("Rb_kategori")
    @Expose
    private String Rb_kategori;
    @SerializedName("id_user")
    @Expose
    private String id_user;

    public rakBuku(String id, String rb_nama, String rb_deskripsi, String rb_author, String rb_pdfUrl, String rb_pdfIcon, String rb_peringkat, String rb_kategori, String id_user) {
        this.id = id;
        Rb_nama = rb_nama;
        Rb_deskripsi = rb_deskripsi;
        Rb_author = rb_author;
        Rb_pdfUrl = rb_pdfUrl;
        Rb_pdfIcon = rb_pdfIcon;
        Rb_peringkat = rb_peringkat;
        Rb_kategori = rb_kategori;
        id_user = id_user;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRb_nama() {
        return Rb_nama;
    }

    public void setRb_nama(String rb_nama) {
        Rb_nama = rb_nama;
    }

    public String getRb_deskripsi() {
        return Rb_deskripsi;
    }

    public void setRb_deskripsi(String rb_deskripsi) {
        Rb_deskripsi = rb_deskripsi;
    }

    public String getRb_author() {
        return Rb_author;
    }

    public void setRb_author(String rb_author) {
        Rb_author = rb_author;
    }

    public String getRb_pdfUrl() {
        return Rb_pdfUrl;
    }

    public void setRb_pdfUrl(String rb_pdfUrl) {
        Rb_pdfUrl = rb_pdfUrl;
    }

    public String getRb_pdfIcon() {
        return Rb_pdfIcon;
    }

    public void setRb_pdfIcon(String rb_pdfIcon) {
        Rb_pdfIcon = rb_pdfIcon;
    }

    public String getRb_peringkat() {
        return Rb_peringkat;
    }

    public void setRb_peringkat(String rb_peringkat) {
        Rb_peringkat = rb_peringkat;
    }

    public String getRb_kategori() {
        return Rb_kategori;
    }

    public void setRb_kategori(String rb_kategori) {
        Rb_kategori = rb_kategori;
    }
}
