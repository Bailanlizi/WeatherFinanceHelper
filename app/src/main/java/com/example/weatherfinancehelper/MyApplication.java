package com.example.weatherfinancehelper;

import android.app.Application;

import com.example.weatherfinancehelper.DatabaseHelper;

public class MyApplication extends Application {
    private DatabaseHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DatabaseHelper(this);
    }

    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }
}