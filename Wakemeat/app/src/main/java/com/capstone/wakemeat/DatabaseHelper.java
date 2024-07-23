package com.capstone.wakemeat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "wake.db";
    public static final String TABLE_NAME_USER = "users_user";
    public static final String TABLE_COLUMN_USER_ID = "id";
    public static final String TABLE_COLUMN_USER_EMAIL = "email";
    public static final String TABLE_COLUMN_USER_PASSWORD = "password";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        /*SQLiteDatabase db = this.getWritableDatabase();*/
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME_USER + " (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_USER);
        this.onCreate(db);
    }

    public boolean insertUser(String email, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_COLUMN_USER_EMAIL, email);
        contentValues.put(TABLE_COLUMN_USER_PASSWORD, password);
        long result = db.insert(TABLE_NAME_USER, null, contentValues);

        if(result == -1) return false;
        else return true;
    }

    public Cursor checkLogin(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT ID FROM "+ TABLE_NAME_USER+ " WHERE email='" + email + "' AND password='" + password + "' LIMIT 1", null);
        return res;
    }
}
