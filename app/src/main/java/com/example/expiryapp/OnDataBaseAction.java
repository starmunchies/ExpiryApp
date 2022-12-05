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

    @Query("DELETE FROM expiry WHERE id = :Id")
    void deleteExpiryFromId(int Id);

    @Query("UPDATE expiry SET profiler = :profiler,heading= :heading WHERE id = :id")
    void updateAnExistingRow(int id, String profiler,String heading);


    // queries used to add to template

    @Query("DELETE FROM template WHERE id = :Id")
    void deleteTemplateFromId(int Id);

    @Insert
    void insertTemplateData(templateGall item);

    @Insert
    void insertExpiryData(items item);

}
