package com.example.expiryapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.expiryapp.ui.gallery.templateGall;
import com.example.expiryapp.ui.home.items;

import java.util.List;

@Dao
public interface OnDataBaseAction {

    @Query("SELECT * FROM Expiry ORDER BY date(date) asc ")
    abstract public List<items> getAll();

    @Query("SELECT * FROM template")
    abstract public List<templateGall> getAllTemplate();

    @Query("DELETE FROM Expiry")
    void deleteTheList();

    @Query("DELETE FROM expiry WHERE id = :taskId")
    void deleteExpiryFromId(int taskId);

    @Query("UPDATE expiry SET profiler = :profiler,heading= :heading WHERE id = :id")
    void updateAnExistingRow(int id, String profiler,String heading);


    // queries used to add to template
    @Insert
    void insertTemplateData(templateGall task);

    @Insert
    void insertExpiryData(items task);

}
