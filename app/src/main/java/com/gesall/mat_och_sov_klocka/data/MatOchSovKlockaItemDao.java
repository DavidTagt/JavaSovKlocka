package com.gesall.mat_och_sov_klocka.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface MatOchSovKlockaItemDao {

    @Insert
    void insert(MatOchSovKlockaItem item);

    @Delete
    void delete(MatOchSovKlockaItem item);

    @Update
    void update(MatOchSovKlockaItem item);

    /*@Query("SELECT * FROM matochsovklockaitem_table ORDER BY created ASC")
    LiveData<List<MatOchSovKlockaItem>> getAllItems();*/

    @Query("SELECT * FROM matochsovklockaitem_table ORDER BY hour, minute ASC")
    LiveData<List<MatOchSovKlockaItem>> getAllItems();
}

