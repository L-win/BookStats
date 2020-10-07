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
    private String dateLastPage;
    private String year;
    private int allPages;

    public Book(String title, String author, String dateAdded, String year, int allPages) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.dateAdded = dateAdded;
        this.allPages = allPages;

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

    public void setDateLastPage(String dateLastPage) {
        this.dateLastPage = dateLastPage;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public String getDateAdded() {
        return dateAdded;
    }
    public String getDateLastPage(){
        return dateLastPage;
    }
    public int getAllPages(){return allPages;}
    public void setAllPages(int allPages){
        this.allPages=allPages;
    }
}
