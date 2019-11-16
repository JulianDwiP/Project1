package com.example.project.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("list")
    @Expose
    private List<SearchModel> searchModelList = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<SearchModel> getSearchModelList() {
        return searchModelList;
    }

    public void setSearchModelList(List<SearchModel> searchModelList) {
        this.searchModelList = searchModelList;
    }
}
