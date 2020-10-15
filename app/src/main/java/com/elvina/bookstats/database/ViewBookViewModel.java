package com.elvina.bookstats.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ViewBookViewModel extends AndroidViewModel {

    private BookRepository repository;

    public ViewBookViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
    }

    public void update(Book book) {
        repository.update(book);
    }

    public LiveData<Book> getSingleBook(int id) {
        return repository.getSingleBook(id);
    }
}
