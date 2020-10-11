package com.elvina.bookstats.database;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookRepository {
    private BookDao bookDao;
    private LiveData<List<Book>> allBooks;

    public BookRepository(Application application) {
        BookDatabase bookDatabase = BookDatabase.getInstance(application);
        bookDao = bookDatabase.bookDao();
        allBooks = bookDao.getAllBooks();
    }

    public void insert(Book book) {
        new InsertBookAsyncTask(bookDao).execute(book);
    }

    public void update(Book book) {
        new UpdateBookAsyncTask(bookDao).execute(book);
    }

    public void delete(Book book) {
        new DeleteBookAsyncTask(bookDao).execute(book);
    }

    public void deleteAllBooks() {
        new DeleteAllBookAsyncTask(bookDao).execute();
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }

    public LiveData<Book> getSingleBook(int id) {
        LiveData<Book> book = null;
        try {
            book = new GetSingleBookAsyncTask(bookDao).execute(id).get();
        } catch (Exception e) {

        }
        return book;
    }

    private static class InsertBookAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;

        private InsertBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.insert(books[0]);
            return null;
        }
    }

    private static class UpdateBookAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;

        private UpdateBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.update(books[0]);
            return null;
        }
    }

    private static class DeleteBookAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;

        private DeleteBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.delete(books[0]);
            return null;
        }
    }

    private static class DeleteAllBookAsyncTask extends AsyncTask<Void, Void, Void> {
        private BookDao bookDao;

        private DeleteAllBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bookDao.deleteAllBooks();
            return null;
        }
    }

    private static class GetSingleBookAsyncTask extends AsyncTask<Integer, Void,
            LiveData<Book>> {
        private BookDao bookDao;

        private GetSingleBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected LiveData<Book> doInBackground(Integer... id) {
            LiveData<Book> book = bookDao.getSingleBook(id[0]);
            return book;
        }

    }
}

