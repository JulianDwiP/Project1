package com.example.project.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class View {
    @SerializedName("pengunjung")
    @Expose
    private int pengunjung;
    @SerializedName("peringkat")
    @Expose
    private float peringkat;

    public float getPeringkat() {
        return peringkat;
    }

    public void setPeringkat(float peringkat) {
        this.peringkat = peringkat;
    }

    public int getPengunjung() {
        return pengunjung;
    }

    public void setPengunjung(int pengunjung) {
        this.pengunjung = pengunjung;
    }
}
