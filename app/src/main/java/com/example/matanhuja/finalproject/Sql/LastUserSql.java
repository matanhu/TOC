package com.example.matanhuja.finalproject.Sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.matanhuja.finalproject.Model.ModelUser;

public class LastUserSql
{
    private static final String LAST_USER_TABLE = "lastUser";
    private static final String USER_EMAIL = "email";
    private static final String USER_UID = "uid";
    private static final String USER_PASSWORD = "password";

    public static void add(SQLiteDatabase db, ModelUser user)
    {
        ContentValues values = new ContentValues();
        values.put(USER_EMAIL,user.getEmail());
        values.put(USER_UID,user.getUid());
        values.put(USER_PASSWORD,user.getPassword());
        db.insert(LAST_USER_TABLE, USER_UID, values);
    }

    public static ModelUser getLastUser(SQLiteDatabase db)
    {
        Cursor cursor = db.query(LAST_USER_TABLE,null, null , null, null, null, null);

        if(cursor.moveToFirst())
        {
            int userUidIndex = cursor.getColumnIndex(USER_UID);
            int userEmailIndex = cursor.getColumnIndex(USER_EMAIL);
            int userPasswordIndex = cursor.getColumnIndex(USER_PASSWORD);

            String userUid = cursor.getString(userUidIndex);
            String userEmail = cursor.getString(userEmailIndex);
            String userPassword = cursor.getString(userPasswordIndex);

            ModelUser user = new ModelUser(userEmail, userPassword, userUid);
            return user;
        }
        return null;
    }

    public static void create(SQLiteDatabase db)
    {
        db.execSQL("create table " +
                LAST_USER_TABLE + " (" +
                USER_EMAIL + " TEXT PRIMARY KEY," +
                USER_PASSWORD + " TEXT," +
                USER_UID + " TEXT);");
    }

    public static boolean checkTableEmpty(SQLiteDatabase db){
        Cursor cur = db.rawQuery("SELECT count(*) FROM " + LAST_USER_TABLE, null);
        if (cur != null && cur.moveToFirst() && cur.getInt(0) > 0) {
            //There is user in cache
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void deleteLastUserTable(SQLiteDatabase db)
    {
        db.delete(LAST_USER_TABLE, null, null);
    }

    public static void drop(SQLiteDatabase db)
    {
        db.execSQL("drop table " + LAST_USER_TABLE);
    }
}
