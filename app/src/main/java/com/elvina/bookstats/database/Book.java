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
    private int currentPage;
    private int allPages;
    private int readingStatus;

    public Book(String title, String author, String dateAdded, String year, int allPages) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.dateAdded = dateAdded;
        this.allPages = allPages;
        this.dateLastPage = dateAdded;
        this.readingStatus = 1;
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

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateLastPage(String dateLastPage) {
        this.dateLastPage = dateLastPage;
    }

    public String getDateLastPage() {
        return dateLastPage;
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

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getAllPages() {
        return allPages;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setReadingStatus(int status){
        this.readingStatus = status;
    }

    public int getReadingStatus(){
        return this.readingStatus;
    }
}
