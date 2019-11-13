package com.example.project.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListSpinnerResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("list")
    @Expose
    private List<ListSpinnerKategori> list = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ListSpinnerKategori> getList() {
        return list;
    }

    public void setList(List<ListSpinnerKategori> list) {
        this.list = list;
    }
}
