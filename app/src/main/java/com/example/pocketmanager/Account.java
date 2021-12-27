package com.example.pocketmanager;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Account {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Property;
    private String InOut;
    private int Price;
    private String CategoryName;
    private String SubCategoryName;
    //private Date Time;
    private String Notation;

    @Ignore
    public Account(String property, String inOut, int price, String categoryName, String subCategoryName, String notation) {
        Property = property;
        InOut = inOut;
        Price = price;
        CategoryName = categoryName;
        SubCategoryName = subCategoryName;
        Notation = notation;
    }

    public Account() {}

    public int getId() {
        return id;
    }
    public String getProperty() {
        return Property;
    }
    public String getInOut() {
        return InOut;
    }
    public int getPrice() {
        return Price;
    }
    public String getCategoryName() {
        return CategoryName;
    }
    public String getSubCategoryName() {
        return SubCategoryName;
    }
    public String getNotation() {
        return Notation;
    }

    public void setProperty(String property) {
        Property = property;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setInOut(String inOut) {
        InOut = inOut;
    }
    public void setPrice(int price) {
        Price = price;
    }
    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }
    public void setNotation(String notation) {
        Notation = notation;
    }
}
