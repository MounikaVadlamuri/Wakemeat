package com.capstone.wakemeat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "wake.db";
    /* USERS_USER */
    public static final String TABLE_NAME_USER = "users_user";
    public static final String TABLE_COLUMN_USER_ID = "id";
    public static final String TABLE_COLUMN_USER_EMAIL = "email";
    public static final String TABLE_COLUMN_USER_PASSWORD = "password";
    /* ALARMS_ALARM */
    public static final String TABLE_NAME_ALARM = "alarms_alarm";
    public static final String TABLE_COLUMN_ALARM_ID = "id";
    public static final String TABLE_COLUMN_ALARM_NAME = "alarm_name";
    public static final String TABLE_COLUMN_ALARM_INITIAL_TIME = "initial_time";
    public static final String TABLE_COLUMN_ALARM_LIMIT_TIME = "limit_time";
    public static final String TABLE_COLUMN_ALARM_LOCATION_LATITUDE = "location_latitude";
    public static final String TABLE_COLUMN_ALARM_LOCATION_LONGITUDE = "location_longitude";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);

        /* If you have to recreate the database uncomment the following lines:
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 0, 1);*/

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME_USER + " ("+TABLE_COLUMN_USER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TABLE_COLUMN_USER_EMAIL+" TEXT, "+TABLE_COLUMN_USER_PASSWORD+" TEXT)");
        db.execSQL("CREATE TABLE "+ TABLE_NAME_ALARM + " ("+TABLE_COLUMN_ALARM_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TABLE_COLUMN_ALARM_NAME+" TEXT, "+TABLE_COLUMN_ALARM_INITIAL_TIME+" TEXT, "+TABLE_COLUMN_ALARM_LIMIT_TIME+" TEXT, "+TABLE_COLUMN_ALARM_LOCATION_LATITUDE+" DOUBLE, "+TABLE_COLUMN_ALARM_LOCATION_LONGITUDE+" DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_ALARM);
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

    public boolean insertAlarm(String name,String initial_time, String limit_time, Double latitude, Double longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_COLUMN_ALARM_NAME, name);
        contentValues.put(TABLE_COLUMN_ALARM_INITIAL_TIME, initial_time);
        contentValues.put(TABLE_COLUMN_ALARM_LIMIT_TIME, limit_time);
        contentValues.put(TABLE_COLUMN_ALARM_LOCATION_LATITUDE, latitude);
        contentValues.put(TABLE_COLUMN_ALARM_LOCATION_LONGITUDE, longitude);
        long result = db.insert(TABLE_NAME_ALARM, null,  contentValues);

        if(result == -1) return false;
        else return true;
    }

    public Cursor listAlarmbyId(Integer alarmId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+ TABLE_NAME_ALARM+ " WHERE "+TABLE_COLUMN_ALARM_ID+"="+ alarmId, null);
        return res;
    }

    public Cursor listAllAlarms(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+ TABLE_NAME_ALARM, null);
        return res;
    }

    public Integer deleteAlarmbyId(Integer alarmId){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME_ALARM, TABLE_COLUMN_ALARM_ID+"="+alarmId, null);
    }
}
