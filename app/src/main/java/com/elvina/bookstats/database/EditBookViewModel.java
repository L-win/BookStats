package com.elvina.bookstats.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class EditBookViewModel extends AndroidViewModel {

    private BookRepository repository;

    public EditBookViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
    }
    public LiveData<Book> getSingleBook(int id) {
        return repository.getSingleBook(id);
    }
}
