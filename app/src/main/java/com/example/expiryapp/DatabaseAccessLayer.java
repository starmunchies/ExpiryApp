// this builds the database and returns the object
package com.example.expiryapp;

import android.content.Context;

import androidx.room.Room;
// reference: https://blog.devgenius.io/implementing-room-database-bc9e4deb6600
// had issues with the database classes given in lab project
// decided to use the database example I found from here
public class DatabaseAccessLayer {
    private Context context;
    private static DatabaseAccessLayer mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseAccessLayer(Context context) {
        this.context = context;

        //create the name of the db and build
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "expiry.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized DatabaseAccessLayer getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseAccessLayer(mCtx);
        }
        return mInstance;
    }


    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
// end reference