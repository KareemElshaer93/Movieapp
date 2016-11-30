package com.example.hp.movieapp.utilities;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.example.hp.movieapp/databases/";

    private static String DB_NAME;
    public SQLiteDatabase myDataBase;
    private final Context myContext;


    public DatabaseHelper(Context context, String dbName) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.DB_NAME = dbName;
        createDataBase();
        openDataBase();
    }


    public void createDataBase() {

        boolean dbExist = checkDataBase();

        if (dbExist) {
        } else {
            this.getReadableDatabase();
            copyDataBase();
        }

    }


    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            close();
            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {
            e.printStackTrace();
            // database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }


    private void copyDataBase() {

        try {
            InputStream myInput = myContext.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            File directory = new File(DB_PATH);
            if (directory.exists() == false) {
                directory.mkdirs();
            }
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void openDataBase() {

        String myPath = DB_PATH + DB_NAME;
        try {
            close();
            myDataBase = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.print("");
        }

    }
   public long insertRow(String table, String whereClause,
                          ContentValues contentArgs) {
        //"WHERE USER.NAME='AHMED AND USER.EMAIL='AHMED92@GMAIL.COM"
        long resu = 0;
        myDataBase.beginTransaction();
        try {
            resu = myDataBase.insert(table, whereClause, contentArgs);
            myDataBase.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("INSERT", e.getMessage());
        } finally {
            myDataBase.endTransaction();
        }

        return resu;
    }

    public Cursor selectAllRaws(String tableName) {

        Cursor cursor = null;
        String sql = "SELECT * FROM " + tableName;
        cursor = myDataBase.rawQuery(sql, new String[]{});

        return cursor;
    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}