package com.elvina.bookstats.ui.finishedbooks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elvina.bookstats.database.Book;
import com.elvina.bookstats.database.Repository;

import java.util.List;

public class FinishedBooksViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<Book>> allFinishedBooks;

    public FinishedBooksViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allFinishedBooks = repository.getAllFinishedBooks();
    }

    public LiveData<List<Book>> getAllFinishedBooks() {
        return allFinishedBooks;
    }

    public void delete(Book book) {
        repository.delete(book);
    }


}
