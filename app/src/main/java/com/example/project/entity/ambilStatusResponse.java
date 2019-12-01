package com.example.project.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ambilStatusResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("list")
    @Expose
    private List<ambilStatus> list = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ambilStatus> getList() {
        return list;
    }

    public void setList(List<ambilStatus> list) {
        this.list = list;
    }
}
