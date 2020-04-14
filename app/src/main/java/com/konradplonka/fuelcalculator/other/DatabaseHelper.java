package com.konradplonka.fuelcalculator.other;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FuelCalculator.db";
    public static final String TABLE_NAME = "FuelDictionary";
    public static final String EXPORT_DICTIONARY = "FuelCalculatorData";


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
    public void clearData(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(String.format("DELETE FROM %s", TABLE_NAME));
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

    public SQLiteCursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteCursor cursor = (SQLiteCursor) db.rawQuery("SELECT * FROM " + TABLE_NAME, null);


        return cursor;
    }

    public boolean exportDatabase(String fileName, Context context){
        File directory = new File(context.getExternalFilesDir(null) + "/" + EXPORT_DICTIONARY);
        if(!directory.exists()){
            directory.mkdirs();
        }

        File file = new File(directory, fileName + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWriter= new CSVWriter(new FileWriter(file));

            DatabaseHelper db = new DatabaseHelper(context);
            Cursor cursor = db.getData();

            csvWriter.writeNext(cursor.getColumnNames());
            while(cursor.moveToNext()){
                String data [] = {
                        String.valueOf(cursor.getInt(0)),
                        cursor.getString(1),
                        String.valueOf(cursor.getInt(2)),
                        String.valueOf(cursor.getDouble(3)),
                        String.valueOf(cursor.getDouble(4)),
                        cursor.getString(5),
                        cursor.getString(6)};
                csvWriter.writeNext(data);

            }
            csvWriter.close();
            cursor.close();
            return true;

        } catch (IOException e) {
            Toast.makeText(context, "Błąd!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }

    }

    public void importDatabase(String backupPath) throws FileNotFoundException {
        File backupFile = new File(backupPath);
        Log.e("Path: ", backupPath);

        if (backupFile.exists()){
            BufferedReader bufferedReader = new BufferedReader(new FileReader(backupFile));

            String csvLine = null;
            String data [];
            try {
                bufferedReader.readLine(); // skip headers
                while((csvLine = bufferedReader.readLine()) != null) {
                    data = csvLine.split(",");
                    Log.e("data", data[1]);
                    addData(
                            data[1].replace("\"", ""),
                            Integer.parseInt(data[2].replace("\"", "")),
                            Double.parseDouble(data[3].replace("\"", "")),
                            Double.parseDouble(data[4].replace("\"", "")),
                            data[5].replace("\"", ""),
                            data[6].replace("\"", "")

                    );
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



