package com.elvina.bookstats.ui.book;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elvina.bookstats.database.Book;
import com.elvina.bookstats.database.BookRepository;

public class BookViewModel extends AndroidViewModel {

    private BookRepository repository;

    public BookViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
    }

    public void insert(Book book){
        repository.insert(book);
    }
    public void update(Book book) {
        repository.update(book);
    }

    public LiveData<Book> getSingleBook(int id) {
        return repository.getSingleBook(id);
    }
}
