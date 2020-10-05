package com.elvina.bookstats.database;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book_table")
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String author;
    private String dateAdded;
    private int year;

    public Book(String title, String author, String dateAdded, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.dateAdded = dateAdded;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public String getDateAdded() {
        return dateAdded;
    }
}
