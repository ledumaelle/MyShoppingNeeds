package com.example.ledumaelle.myshoppingneeds.dal;

import android.content.Context;

import androidx.room.Room;

public class Connection {

    private static final String DATABASE_NAME = "myShoppingsNeedsRoom_db";

    public static AppDatabase getConnexion(Context context) {

        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }
}
