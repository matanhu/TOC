package com.example.matanhuja.finalproject.Sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;

public class EventsUserSql
{
    private static final String EVENTS_USER_TABLE = "eventsUser";
    private static final String EVENTS_USER_EMAIL = "email";
    private static final String EVENTS_USER_EVENT = "eventId";

    public static void add(SQLiteDatabase db, String email, String event_id)
    {
        ContentValues values = new ContentValues();
        values.put(EVENTS_USER_EMAIL,email);
        values.put(EVENTS_USER_EVENT, event_id);
        db.insert(EVENTS_USER_TABLE, null, values);
    }

    public static void delete(SQLiteDatabase db, String event_id)
    {
        String[] params = new String[1];
        params[0] = event_id;
        db.delete(EVENTS_USER_TABLE, EVENTS_USER_EVENT + " = ?", params);
    }

    public static List<String> getEvents(SQLiteDatabase db, String email)
    {
        String[] params = new String[1];
        params[0] = email;
        Cursor cursor = db.query(EVENTS_USER_TABLE,null, EVENTS_USER_EMAIL + "= ?", params, null, null, null);

        List<String> list = new LinkedList<String>();
        if(cursor.moveToFirst())
        {
            int userEmailIndex = cursor.getColumnIndex(EVENTS_USER_EMAIL);
            int userEventIndex = cursor.getColumnIndex(EVENTS_USER_EVENT);
            do
            {
                String eventId = cursor.getString(userEventIndex);
                String userEmail = cursor.getString(userEmailIndex);
                list.add(eventId);
            }
            while (cursor.moveToNext());
        }
        return list;

    }

    public static void create(SQLiteDatabase db)
    {
        db.execSQL("create table " +
                EVENTS_USER_TABLE + " (" +
                EVENTS_USER_EMAIL + " TEXT , " +
                EVENTS_USER_EVENT + " TEXT , " +
                "PRIMARY KEY (" + EVENTS_USER_EMAIL + "," + EVENTS_USER_EVENT + "));");
    }
    public static void drop(SQLiteDatabase db) {
        db.execSQL("drop table " + EVENTS_USER_TABLE);
    }
}
