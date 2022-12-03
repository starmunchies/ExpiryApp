package com.example.expiryapp.ui.home;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "Expiry")
public class items implements Serializable {

    @PrimaryKey(autoGenerate  = true)
    private int id;

    @ColumnInfo(name = "heading")
    public String heading;


    @ColumnInfo(name = "profiler")
    public String profiler;

    @ColumnInfo(name = "date")
    // unable to save date as an object thus I save to a string
    // and parse it to an object instead
    public String expiryDate;

    // returns the expiry date that has been set
    public Date getExpiryDate() throws ParseException {
        SimpleDateFormat fd = new SimpleDateFormat ("yyyy-MM-dd");
        Date objectDate = fd.parse(expiryDate);

        return objectDate;
    }

    public void setExpiryDate(String expiryDate) throws ParseException {
        //  sets the format that the date is represented in string format
       this.expiryDate = expiryDate;
    }


    // isn't needed only used for testing purposes
    public void setId(int id) {
        this.id = id;
    }

    // this will get the absolute path for the image icon once set
    public String getProfiler() {
        return profiler;
    }

    // will set the absolute path
    public void setProfiler(String profiler) {
        this.profiler = profiler;
    }

    public int getId(){
        return id;
    }

// allows us to set an initial heading for the item
    public items(String heading) {
        this.heading = heading;
    }
    public items() {

    }
    // gets the heading for the item adapter class
    public String getHeading() {return heading;}

    // allows us to update the header in the future i.e to rename
    public void setHeading(String heading) {
        this.heading = heading;
    }

}
