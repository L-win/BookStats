package com.elvina.bookstats.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookDao {
    @Insert
    void insert(Book book);
    @Update
    void update(Book book);
    @Delete
    void delete(Book book);

    @Query("DELETE FROM book_table")
    void deleteAllBooks();

    @Query("SELECT * FROM book_table where readingStatus = 1 ORDER BY id DESC")
    LiveData<List<Book>> getAllBooks();

    @Query("SELECT * FROM book_table WHERE id = :id")
    LiveData<Book> getSingleBook(int id);

    @Query("SELECT * FROM book_table WHERE id = :id")
    Book getSingleBookMutable(int id);

    @Query("SELECT * FROM book_table WHERE readingStatus = 0 ORDER BY id DESC")
    LiveData<List<Book>> getAllFinishedBooks();




}
