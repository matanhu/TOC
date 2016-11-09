package com.example.matanhuja.finalproject.Sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.matanhuja.finalproject.Model.ModelUser;

import java.util.LinkedList;
import java.util.List;

public class UserSql
{
    private static final String USERS_TABLE = "users";
    private static final String USER_EMAIL = "email";
    private static final String USER_UID = "uid";

    public static void add(SQLiteDatabase db, ModelUser user)
    {
        ContentValues values = new ContentValues();
        values.put(USER_EMAIL,user.getEmail());
        values.put(USER_UID,user.getUid());
        db.insert(USERS_TABLE, USER_UID, values);
    }

    public static ModelUser getUser(SQLiteDatabase db, String userId)
    {
        String[] params = new String[1];
        params[0] = userId;
        Cursor cursor = db.query(USERS_TABLE,null, USER_UID + "= ?", params, null, null, null);
        if(cursor.moveToFirst())
        {
            int userUidIndex = cursor.getColumnIndex(USER_UID);
            int userEmailIndex = cursor.getColumnIndex(USER_EMAIL);

            String userUid = cursor.getString(userUidIndex);
            String userEmail = cursor.getString(userEmailIndex);

            ModelUser user = new ModelUser();
            user.modelUserToLocalDb(userEmail,userUid);
            return user;
        }
        return null;
    }

    public static List<ModelUser> getUsers(SQLiteDatabase db)
    {
        Cursor cursor = db.query(USERS_TABLE, null, null, null, null, null, null);

        List<ModelUser> list = new LinkedList<ModelUser>();
        if(cursor.moveToFirst())
        {
            int userUidIndex = cursor.getColumnIndex(USER_UID);
            int userEmailIndex = cursor.getColumnIndex(USER_EMAIL);
            do
            {
                String userUid = cursor.getString(userUidIndex);
                String userEmail = cursor.getString(userEmailIndex);

                ModelUser user = new ModelUser();
                user.modelUserToLocalDb(userEmail, userUid);
                list.add(user);
            }
            while (cursor.moveToNext());
        }
        return list;
    }

    public static void create(SQLiteDatabase db)
    {
        db.execSQL("create table " +
                    USERS_TABLE + " (" +
                    USER_EMAIL + " TEXT PRIMARY KEY," +
                    USER_UID + " TEXT);");
    }

    //Get last update - used to compare against firebase
    public static String getLastUpdateDate(SQLiteDatabase db, String userEmail)
    {
        return LastUpdateSql.getLastUpdate(db,userEmail);
    }

    //Set last update
    public static void setLastUpdateDate(SQLiteDatabase db, String date, String userEmail)
    {
        LastUpdateSql.setLastUpdate(db,userEmail, date);
    }

    public static void drop(SQLiteDatabase db)
    {
        db.execSQL("drop table " + USERS_TABLE);
    }
}
