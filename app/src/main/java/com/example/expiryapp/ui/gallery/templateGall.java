// used for template table object
// lets us save templates to be used in the future to invoke items
// of similar type
package com.example.expiryapp.ui.gallery;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName = "template")
public class templateGall implements Serializable {



        @PrimaryKey(autoGenerate  = true)
        private int id;
        //sets the heading
        @ColumnInfo(name = "heading")
        public String heading;

        // sets the avatar icon
        @ColumnInfo(name = "profiler")
        public String profiler;

        // sets no days the item would be viable for
        @ColumnInfo(name = "days")
        public String expiryDate;

        // returns the expiry date that has been set
        public String getExpiryDate() throws ParseException {
            return expiryDate;
        }
        //sets the expiry date
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
        public templateGall(){

        }
        // gets the heading for the item adapter class
        public String getHeading() {return heading;}

        // allows us to update the header in the future i.e to rename
        public void setHeading(String heading) {
            this.heading = heading;
        }

    }



