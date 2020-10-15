package com.elvina.bookstats.ui.finishedbooks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elvina.bookstats.database.Book;
import com.elvina.bookstats.database.BookRepository;

import java.util.List;

public class FinishedBooksViewModel extends AndroidViewModel {

    private BookRepository repository;
    private LiveData<List<Book>> allFinishedBooks;

    public FinishedBooksViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
        allFinishedBooks = repository.getAllFinishedBooks();
    }

    public LiveData<List<Book>> getAllFinishedBooks() {

        return allFinishedBooks;
    }


}
