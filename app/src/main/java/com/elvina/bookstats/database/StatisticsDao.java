package com.elvina.bookstats.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StatisticsDao {

    @Insert
    void insert(Statistics stats);
    @Update
    void update(Statistics stats);

    @Query("SELECT * FROM statistics_table")
    List<Statistics> getAllStatistics();

}
