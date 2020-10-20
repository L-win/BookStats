package com.elvina.bookstats.database;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Book.class, Statistics.class}, version = 1)
public abstract class BookDatabase extends RoomDatabase {
    private static BookDatabase instance;

    public abstract BookDao bookDao();

    public abstract StatisticsDao statisticsDao();

    public static synchronized BookDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BookDatabase.class,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private BookDao bookDao;
        private StatisticsDao statisticsDao;

        private PopulateDbAsyncTask(BookDatabase db) {
            bookDao = db.bookDao();
            statisticsDao = db.statisticsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Book bookA = new Book(
                    "War and Peace",
                    "Leo Tolstoy",
                    "1867",
                    1600);
            bookA.setCoverUri("");
            bookA.setDateAdded("Thu Sep 28 20:21:30 UTC 2020");
            bookDao.insert(bookA);

            Statistics statsA = new Statistics("sumAllAddedBooks",0);
            Statistics statsB = new Statistics("sumAllReadingBooks",0);
            Statistics statsC = new Statistics("sumAllFinishedBooks",0);
            statisticsDao.insert(statsA);
            statisticsDao.insert(statsB);
            statisticsDao.insert(statsC);

            return null;
        }
    }
}

