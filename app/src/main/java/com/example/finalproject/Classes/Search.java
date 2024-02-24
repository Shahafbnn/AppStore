package com.example.finalproject.Classes;

import java.util.Date;

public class Search {
    private String searchText;
    private String searchId;
    private Date searchTime;

    public Search() {
    }

    public Search(String searchText, Date searchTime) {
        this.searchText = searchText;
        this.searchTime = searchTime;
    }

    public Date getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(Date searchTime) {
        this.searchTime = searchTime;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }
}
