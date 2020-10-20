package com.elvina.bookstats.ui.statistics;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.elvina.bookstats.database.BookRepository;

public class StatisticsViewModel extends AndroidViewModel{
        private BookRepository repository;

    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
    }

}
