package com.example.lostandfoundapp.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.lostandfoundapp.model.Advertisement;
import com.example.lostandfoundapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, 2);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "(" +
                Util.ADVERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                Util.SELECTED_OPTION + " TEXT, " +
                Util.NAME + " TEXT, " +
                Util.PHONE + " TEXT, " +
                Util.DESCRIPTION + " TEXT, " +
                Util.DATE + " TEXT, " +
                Util.LATITUDE + " REAL, "+
                Util.LONGITUDE + " REAL)";
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
    }

    // Insert an advert into the database
    public long insertAdvertisement(Advertisement advert) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.SELECTED_OPTION, advert.getSelectedOption());
        contentValues.put(Util.NAME, advert.getName());
        contentValues.put(Util.PHONE, advert.getPhone());
        contentValues.put(Util.DESCRIPTION, advert.getDescription());
        contentValues.put(Util.DATE, advert.getDate());
        contentValues.put(Util.LATITUDE, advert.getLatitude());
        contentValues.put(Util.LONGITUDE, advert.getLongitude());

        long newRowId = db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();

        return newRowId;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + Util.TABLE_NAME + " ADD COLUMN " + Util.SELECTED_OPTION + " TEXT");
        }
    }

    public Advertisement getAdvertisementByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Util.TABLE_NAME, new String[]{Util.SELECTED_OPTION, Util.NAME, Util.PHONE, Util.DESCRIPTION, Util.DATE, Util.LATITUDE,Util.LONGITUDE},
                Util.NAME + "=?", new String[]{name}, null, null, null, null);
        Advertisement advert = null;
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String selected_option = cursor.getString(cursor.getColumnIndex(Util.SELECTED_OPTION));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(Util.PHONE));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(Util.DESCRIPTION));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(Util.DATE));
            @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(Util.LATITUDE));
            @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(Util.LONGITUDE));
            advert = new Advertisement(selected_option, name, phone, description, date, latitude,longitude);
            cursor.close();
        }
        db.close();
        return advert;
    }
    public int deleteAdvertisementByName(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(Util.TABLE_NAME, Util.NAME + " = ?", new String[]{name});
    }
    public List<Advertisement> getAllAdvertisements() {
        List<Advertisement> advertisements = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {Util.ADVERT_ID, Util.SELECTED_OPTION, Util.NAME, Util.PHONE, Util.DESCRIPTION, Util.DATE, Util.LATITUDE,Util.LONGITUDE};
        // Query the table and return a cursor to the results
        Cursor cursor = db.query(Util.TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String selected_option = cursor.getString(cursor.getColumnIndex(Util.SELECTED_OPTION));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(Util.PHONE));
            @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(Util.DESCRIPTION));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(Util.DATE));
            @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(Util.LATITUDE));
            @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(Util.LONGITUDE));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Util.NAME));
            Advertisement advertisement = new Advertisement(selected_option, name, phone, description, date, latitude, longitude);
            advertisements.add(advertisement);
        }
        cursor.close();
        db.close();
        return advertisements;
    }
    public Cursor getAllAdvertisementsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {Util.ADVERT_ID, Util.SELECTED_OPTION, Util.NAME, Util.PHONE, Util.DESCRIPTION, Util.DATE, Util.LATITUDE,Util.LONGITUDE};
        // Query the table and return a cursor to the results
        Cursor cursor = db.query(Util.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }
}
