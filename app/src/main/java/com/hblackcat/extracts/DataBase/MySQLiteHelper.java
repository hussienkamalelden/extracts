package com.hblackcat.extracts.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import java.io.File;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.hblackcat.extracts/databases/";
    private static final String DATABASE_NAME = "extract.db";
    private static final int DATABASE_VERSION = 2;
    private SQLiteDatabase myDataBase;
    private Context context;

    public MySQLiteHelper(Context contextX) {
        super(contextX, DATABASE_NAME, null, DATABASE_VERSION);
        context = contextX;
    }

    // onCraete database ..
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    // onUpdate database ..
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // Creates a empty database on the system if not exist ..
    public void createDataBase(){
        //check database exist ..
        boolean dbExist = checkDataBase();
        if(dbExist){
            //do nothing - database already exist
        }else{
            // if not exist ..
            try {
                //create database with three column ..
                myDataBase =getWritableDatabase();
                // make name unique to avoid repeat it ..
                String create_table = "CREATE TABLE extract_tbl(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL UNIQUE, value TEXT NOT NULL)";
                myDataBase.execSQL(create_table);

                // insert all fixed values in column name ..
                fixedValues();
            } catch (Exception e) {}
        }
    }

    // Check if the database already exist ..@return true if it exists, false if it doesn't
    private boolean checkDataBase(){
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    //open database ..
    public void openDataBase(){
        try {
            //Open the database
            String myPath = DB_PATH + DATABASE_NAME;
            myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch (Exception e){e.printStackTrace();}
    }

    //insert into database fixed unique names and value = false ..
    public void insertIntoDBNames(String name,String value)
    {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues content_values = new ContentValues();
            content_values.put("name", name);
            content_values.put("value", value);
            db.insert("extract_tbl", null, content_values);
            db.close();
        }catch (Exception e){e.printStackTrace();}
    }

    //replace value method ..
    public void replaceValue(String name , String new_value) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "UPDATE extract_tbl SET value='"+new_value+"' WHERE name='"+name+"'";
            db.execSQL(query);
        }catch (Exception e){e.printStackTrace();}
    }

    // insert all fixed values in columns name and value ..
    public void fixedValues()
    {
        try
        {
            /////////////////////////////////// Tab1 ///////////////////////////////////
            //Logo path
            insertIntoDBNames("logo_path","..........");
            //Rectangle Horizontal
            insertIntoDBNames("rec_hor","false");
            insertIntoDBNames("rec_hor_height","50");
            insertIntoDBNames("rec_hor_width","100");
            //Rectangle Vertical
            insertIntoDBNames("rec_ver","false");
            insertIntoDBNames("rec_ver_height","100");
            insertIntoDBNames("rec_ver_width","50");
            //Square
            insertIntoDBNames("square","false");
            insertIntoDBNames("square_height","100");
            insertIntoDBNames("square_width","100");
            //EditTexts
            insertIntoDBNames("extract_num_edit","..........");
            insertIntoDBNames("to_gentleman_edit","..........");
            insertIntoDBNames("operation_edit","..........");
            insertIntoDBNames("about_edit","..........");
            insertIntoDBNames("attachments_edit","..........");
            //Date
            insertIntoDBNames("from_date","..........");
            insertIntoDBNames("to_date","..........");

            /////////////////////////////////// Tab2 ///////////////////////////////////
            insertIntoDBNames("inventory_eng_edit","..........");
            insertIntoDBNames("operation_eng_edit","..........");
            insertIntoDBNames("quality_eng_edit","..........");
            insertIntoDBNames("technical_dir_edit","..........");
            insertIntoDBNames("accounts_edit","..........");
            insertIntoDBNames("project_man_edit","..........");

            /////////////////////////////////////// Tab3 ///////////////////////////////////////
            //we insert it in onDestroy Tab3 because it will insert only one time ..

            /////////////////////////////////// Tab4 ///////////////////////////////////
            //we insert it in onDestroy Tab3 because it will insert only one time ..

            /////////////////////////////////////// NewReport ///////////////////////////////////////
            //we insert it in NewReport.class because it will insert only one time ..

        } catch (Exception e) {e.printStackTrace();}
    }

    //getValue Search in DataBase ..
    public String getValue(String name) {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder ();

            //table name ..
            queryBuilder.setTables("extract_tbl");

            // Search in column 1 where name like ..
            String selectQuery = " select * from extract_tbl where name like  '%"+ name +"%'";

            // Initialize Cursor ..
            Cursor mCursor = db.rawQuery(selectQuery, null);

            //Search in all table start from the top of column ..
            if(mCursor.moveToFirst())
            {
                do{
                    return mCursor.getString(mCursor.getColumnIndex("value"));
                }while (mCursor.moveToNext());
            }
        }catch (Exception e){}
        return null;
    }

    //Drop database if exist ..
    public void dropDataBase()
    {
        //check database exist ..
        boolean dbExist = checkDataBase();

        if(dbExist) {
            //database already exist .. delete it
            context.deleteDatabase(DATABASE_NAME);
        }

        //then close database after drop ..
        close();
    }

    //close database ..
    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }
}