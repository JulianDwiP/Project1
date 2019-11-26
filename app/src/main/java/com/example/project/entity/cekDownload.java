package com.example.project.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class cekDownload {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("hasil")
    @Expose
    private Hasil hasil;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Hasil getHasil() {
        return hasil;
    }

    public void setHasil(Hasil hasil) {
        this.hasil = hasil;
    }
}
