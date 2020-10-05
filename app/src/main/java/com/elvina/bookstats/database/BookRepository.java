package com.elvina.bookstats.database;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {
    private BookDao bookDao;
    private LiveData<List<Book>> allBooks;

    public BookRepository(Application application){
        BookDatabase bookDatabase = BookDatabase.getInstance(application);
        bookDao = bookDatabase.bookDao();
        allBooks = bookDao.getAllBooks();
    }
    public void insert(Book book){
        new InsertNoteAsyncTask(bookDao).execute(book);
    }
    public void update(Book book){
        new UpdateNoteAsyncTask(bookDao).execute(book);
    }
    public void delete(Book book){
        new DeleteNoteAsyncTask(bookDao).execute(book);
    }
    public void deleteAllBooks(){
        new DeleteAllNoteAsyncTask(bookDao).execute();
    }
    public LiveData<List<Book>> getAllBooks(){
        return allBooks;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;
        private InsertNoteAsyncTask(BookDao bookDao){
            this.bookDao = bookDao;
        }
        @Override
        protected Void doInBackground(Book... books){
            bookDao.insert(books[0]);
            return null;
        }
    }
    private static class UpdateNoteAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;
        private UpdateNoteAsyncTask(BookDao bookDao){
            this.bookDao = bookDao;
        }
        @Override
        protected Void doInBackground(Book... books){
            bookDao.update(books[0]);
            return null;
        }
    }
    private static class DeleteNoteAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;
        private DeleteNoteAsyncTask(BookDao bookDao){
            this.bookDao = bookDao;
        }
        @Override
        protected Void doInBackground(Book... books){
            bookDao.delete(books[0]);
            return null;
        }
    }
    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private BookDao bookDao;
        private DeleteAllNoteAsyncTask(BookDao bookDao){
            this.bookDao = bookDao;
        }
        @Override
        protected Void doInBackground(Void... voids){
            bookDao.deleteAllBooks();
            return null;
        }
    }
}

