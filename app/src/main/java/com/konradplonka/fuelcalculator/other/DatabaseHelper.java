package com.konradplonka.fuelcalculator.other;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

import com.konradplonka.fuelcalculator.PetrolStations;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FuelCalculator.db";
    public static final String TABLE_NAME = "FuelDictionary";

    public DatabaseHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s " +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "PetrolStation TEXT," +
                "Distance INTEGER, " +
                "AmountOfFuel DOUBLE, " +
                "TotalCost DOUBLE, " +
                "Date TEXT, " +
                "Description TEXT);", TABLE_NAME));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP IF TABLE EXISTS %s", TABLE_NAME));
        onCreate(db);
    }

    public boolean addData(String petrolStation, int distance, double amountOfFuel, double totalCost, String date, String description){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues  = new ContentValues();

        contentValues.put("PetrolStation", petrolStation);
        contentValues.put("Distance", distance);
        contentValues.put("AmountOfFuel", amountOfFuel);
        contentValues.put("TotalCost", totalCost);
        contentValues.put("Date", date);
        contentValues.put("Description", description);

        if(db.insert(TABLE_NAME, null, contentValues) == 1){
            return true;
        }
        else return false;
    }
    public boolean deleteData(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        if(db.delete(TABLE_NAME, "ID= ?",  new String[]{String.valueOf(id)}) >0){
            return true;
        }
        return false;
    }
    public boolean updateData(int id, String petrolStation, int distance, double amountOfFuel, double totalCost, String date, String description){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("PetrolStation", petrolStation);
        contentValues.put("Distance", distance);
        contentValues.put("AmountOfFuel", amountOfFuel);
        contentValues.put("TotalCost", totalCost);
        contentValues.put("Date", date);
        contentValues.put("Description", description);

        if(db.update(TABLE_NAME, contentValues, "ID=?", new String[]{String.valueOf(id)})>0){
            return true;
        }
        return false;


    }

    public SQLiteCursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteCursor cursor = (SQLiteCursor) db.rawQuery("SELECT * FROM " + TABLE_NAME, null);


        return cursor;
    }


}
