package com.example.finalproject.Classes.Category;

import java.util.ArrayList;

public class Categories {
    private ArrayList<String> categories;

    public Categories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public Categories() {
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}
