package com.example.pocketmanager;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity
public class Account {
    @PrimaryKey(autoGenerate = true)
    private int Id;
    private String Property;
    private String InOut;
    private int Amount;
    private String Category;
    private String SubCategory;

    private int Year;
    private int Month;
    private int Day;
    private int Hour;
    private int Minute;
    private String Note;

    @Ignore
    public Account(String property, String inOut, int amount, String category, String subCategory, int year, int month, int day, int hour, int minute, String note) {
        Property = property;
        InOut = inOut;
        Amount = amount;
        Category = category;
        SubCategory = subCategory;
        Year = year;
        Month = month;
        Day = day;
        Hour = hour;
        Minute = minute;
        Note = note;
    }

    @Ignore
    public Account(int id, String property, String inOut, int amount, String category, String subCategory, int year, int month, int day, int hour, int minute, String note) {
        Id = id;
        Property = property;
        InOut = inOut;
        Amount = amount;
        Category = category;
        SubCategory = subCategory;
        Year = year;
        Month = month;
        Day = day;
        Hour = hour;
        Minute = minute;
        Note = note;
    }

    @Ignore
    public Account(int id) {
        Id = id;
    }

    public Account() {}

    public int getId() {
        return Id;
    }
    public String getProperty() {
        return Property;
    }
    public String getInOut() {
        return InOut;
    }
    public int getAmount() {
        return Amount;
    }
    public String getCategory() {
        return Category;
    }
    public String getSubCategory() {
        return SubCategory;
    }
    public String getNote() {
        return Note;
    }
    public int getYear() { return Year; }
    public int getMonth() { return Month; }
    public int getDay() { return Day; }
    public int getHour() { return Hour; }
    public int getMinute() { return Minute; }

    public void setProperty(String property) {
        Property = property;
    }
    public void setId(int id) {
        this.Id = id;
    }
    public void setInOut(String inOut) {
        InOut = inOut;
    }
    public void setAmount(int amount) {
        Amount = amount;
    }
    public void setCategory(String category) {
        Category = category;
    }
    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }
    public void setNote(String note) {
        Note = note;
    }
    public void setYear(int year) { Year = year; }
    public void setMonth(int month) { Month = month; }
    public void setDay(int day) { Day = day; }
    public void setHour(int hour) { Hour = hour; }
    public void setMinute(int minute) { Minute = minute; }
}
