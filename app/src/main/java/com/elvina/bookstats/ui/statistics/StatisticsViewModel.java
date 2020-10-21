package com.elvina.bookstats.ui.statistics;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.elvina.bookstats.database.BookRepository;
import com.elvina.bookstats.database.Statistics;

import java.util.List;

public class StatisticsViewModel extends AndroidViewModel{
        private BookRepository repository;

    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
    }


    public LiveData<List<Statistics>> getAllStatistics(){
        return this.repository.getAllStats();
    }
}
