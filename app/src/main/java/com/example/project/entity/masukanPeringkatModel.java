package com.example.project.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class masukanPeringkatModel {
    @SerializedName("peringkat")
    @Expose
    private String peringkat;

    public masukanPeringkatModel(String peringkat) {
        this.peringkat = peringkat;
    }

    public String getPeringkat() {
        return peringkat;
    }

    public void setPeringkat(String peringkat) {
        this.peringkat = peringkat;
    }
}
