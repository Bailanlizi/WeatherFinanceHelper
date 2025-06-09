package com.example.weatherfinancehelper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "finance.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_RECORDS = "records";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_RECORDS + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "type INTEGER, amount REAL, tag TEXT, date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addRecord(FinanceRecord record) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("type", record.getAmount() >= 0 ? 0 : 1);
        values.put("amount", Math.abs(record.getAmount()));
        values.put("tag", record.getTag());
        values.put("date", record.getDate());
        return db.insert(TABLE_RECORDS, null, values);
    }

    @SuppressLint("Range")
    public List<FinanceRecord> getAllRecords() {
        List<FinanceRecord> records = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                TABLE_RECORDS, null, null, null, null, null, "date DESC");

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex("type"));
            @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
            records.add(new FinanceRecord(
                    cursor.getString(cursor.getColumnIndex("tag")),
                    type == 0 ? amount : -amount,
                    cursor.getString(cursor.getColumnIndex("date"))
            ));
        }
        cursor.close();
        return records;
    }
}