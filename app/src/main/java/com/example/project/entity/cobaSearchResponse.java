package com.example.project.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class cobaSearchResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("list")
    @Expose
    private List<cobaSearchModel> list;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<cobaSearchModel> getList() {
        return list;
    }

    public void setList(List<cobaSearchModel> list) {
        this.list = list;
    }
}
