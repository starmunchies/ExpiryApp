package com.example.expiryapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.expiryapp.ui.home.items;

import java.util.List;

@Dao
public interface OnDataBaseAction {

    @Query("SELECT * FROM Expiry")
    abstract public List<items> getAll();

    @Query("DELETE FROM Expiry")
    void deleteTheList();

    @Insert
    void insertExpiryData(items task);

    @Query("DELETE FROM expiry WHERE id = :taskId")
    void deleteExpiryFromId(int taskId);

    @Query("UPDATE expiry SET profiler = :profiler WHERE id = :id")
    void updateAnExistingRow(int id, String profiler);

}
