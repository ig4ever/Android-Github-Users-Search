package com.rakhmat.androidgithubuserssearch.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUser {
    @SerializedName("items")
    List<User> listDataUser;
    @SerializedName("total_count")
    String totalCount;

    public List<User> getListDataUser() {
        return listDataUser;
    }
    public void setListDataUser(List<User> listDataUser) {
        this.listDataUser = listDataUser;
    }
    public String getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }
}
