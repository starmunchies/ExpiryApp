//this abstract class allows for implementation of these functions
// that are connected to the DAO
package com.example.expiryapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.expiryapp.ui.gallery.templateGall;
import com.example.expiryapp.ui.home.items;

import java.util.List;

@Dao
public interface OnDataBaseAction {
    // selects all and orders by date
    @Query("SELECT * FROM Expiry ORDER BY date(date) asc ")
    abstract public List<items> getAll();
    // selects all from templates
    @Query("SELECT * FROM template")
    abstract public List<templateGall> getAllTemplate();
    // deletes everything and truncates the array
    @Query("DELETE FROM Expiry")
    void deleteTheList();

    // deletes from expiry table based on id
    @Query("DELETE FROM expiry WHERE id = :Id")
    void deleteExpiryFromId(int Id);
    // updates expiry table based on heading and image profiler
    @Query("UPDATE expiry SET profiler = :profiler,heading= :heading WHERE id = :id")
    void updateAnExistingRow(int id, String profiler,String heading);


   // deletes from template
    @Query("DELETE FROM template WHERE id = :Id")
    void deleteTemplateFromId(int Id);

    // inserts a template into the template table
    @Insert
    void insertTemplateData(templateGall item);

    // inserts an item into the expiry data
    @Insert
    void insertExpiryData(items item);

}
