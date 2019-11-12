package com.example.project.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class rakBukuResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("list")
    @Expose
    private List<rakBuku> rakBukuList = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<rakBuku> getRakBukuList() {
        return rakBukuList;
    }

    public void setRakBukuList(List<rakBuku> rakBukuList) {
        this.rakBukuList = rakBukuList;
    }
}
