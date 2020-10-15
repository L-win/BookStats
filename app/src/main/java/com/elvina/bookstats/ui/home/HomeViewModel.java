package com.elvina.bookstats.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elvina.bookstats.database.Book;
import com.elvina.bookstats.database.BookRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private BookRepository repository;
    private LiveData<List<Book>> allBooks;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
        allBooks = repository.getAllBooks();
    }

    public void insert(Book book) {
        repository.insert(book);
    }

    public void update(Book book) {
        repository.update(book);
    }

    public void delete(Book book) {
        repository.delete(book);
    }

    public void deleteAllBooks() {
        repository.deleteAllBooks();
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }



}
