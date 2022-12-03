package com.example.expiryapp;

import android.content.Context;

import androidx.room.Room;

public class DatabaseAccessLayer {
    private Context mCtx;
    private static DatabaseAccessLayer mInstance;

    //our app database object
    private AppDatabase appDatabase;

    private DatabaseAccessLayer(Context mCtx) {
        this.mCtx = mCtx;
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "expiry.db")
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
