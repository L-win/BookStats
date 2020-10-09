package com.elvina.bookstats.database;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

//TODO: RENAME NOTEASYNCTASK TO BOOK

public class BookRepository {
    private BookDao bookDao;
    private LiveData<List<Book>> allBooks;
    private LiveData<Book> singleBook;

    public BookRepository(Application application) {
        BookDatabase bookDatabase = BookDatabase.getInstance(application);
        bookDao = bookDatabase.bookDao();
        allBooks = bookDao.getAllBooks();
    }

    public void insert(Book book) {
        new InsertNoteAsyncTask(bookDao).execute(book);
    }

    public void update(Book book) {
        new UpdateNoteAsyncTask(bookDao).execute(book);
    }

    public void delete(Book book) {
        new DeleteNoteAsyncTask(bookDao).execute(book);
    }

    public void deleteAllBooks() {
        new DeleteAllNoteAsyncTask(bookDao).execute();
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }

    public LiveData<Book> getSingleBook(int id) {
        LiveData<Book> book = null;
        try{
            book = new GetSingleBookAsyncTask(bookDao).execute(id).get();
        }catch (Exception e){

        }
        return book;
    }
    public LiveData<Book> asyncResult(LiveData<Book> book){
        return this.singleBook = book;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;

        private InsertNoteAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.insert(books[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;

        private UpdateNoteAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.update(books[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Book, Void, Void> {
        private BookDao bookDao;

        private DeleteNoteAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.delete(books[0]);
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private BookDao bookDao;

        private DeleteAllNoteAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bookDao.deleteAllBooks();
            return null;
        }
    }

    private static class GetSingleBookAsyncTask extends  AsyncTask<Integer,Void,
            LiveData<Book>>{
        private BookDao bookDao;
//        private LiveData<Book> book;

        private GetSingleBookAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }
        @Override
        protected LiveData<Book> doInBackground(Integer... id) {
//            bookDao.deleteAllBooks();
            LiveData<Book> book = bookDao.getSingleBook(id[0]);
            return book;
        }

    }


//    private static class GetSingleBookAsyncTask extends  AsyncTask<Integer,Void,
//            Void>{
//        private BookDao bookDao;
//        private LiveData<Book> book;
//
//        private GetSingleBookAsyncTask(BookDao bookDao) {
//            this.bookDao = bookDao;
//        }
//        @Override
//        protected Void doInBackground(Integer... id) {
////            bookDao.deleteAllBooks();
//            this.book = bookDao.getSingleBook(id[0]);
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Void result) {
//            //do stuff
//            asyncResult(myValue);
//        }
//
//    }
}

