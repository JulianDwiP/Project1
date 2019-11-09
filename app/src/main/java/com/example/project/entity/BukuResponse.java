package com.example.project.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BukuResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("list")
    @Expose
    private List<Buku> list= null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Buku> getList() {
        return list;
    }

    public void setList(List<Buku> list) {
        this.list = list;
    }
}
