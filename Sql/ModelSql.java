package com.example.matanhuja.finalproject.Sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.matanhuja.finalproject.Model.ModelEvent;
import com.example.matanhuja.finalproject.Model.ModelUser;
import com.example.matanhuja.finalproject.MyApplication;

import java.util.List;

public class ModelSql
{
    private static final int VERSION = 1;

    MyDBHelper dbHelper;

    public ModelSql()
    {
        dbHelper = new MyDBHelper(MyApplication.getAppContext());
    }

    public ModelSql(Context context)
    {
        dbHelper = new MyDBHelper(context);
    }

    public void addEvent(ModelEvent event, String email)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        EventSql.add(db, event, email);
    }

    public void updateEvent(ModelEvent event)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        EventSql.update(db, event);
    }

    public ModelEvent getEvent(String id)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return EventSql.getEvent(db,id);
    }

    public List<ModelEvent> getEvents(String email)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return EventSql.getEvents(db, email);
    }

    public  List<String> getUserEventsId(String email)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return EventsUserSql.getEvents(db,email);
    }

    public void delete(ModelEvent event)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        EventSql.delete(db, event);
        EventsUserSql.delete(db, String.valueOf(event.getEventId()));
    }

    public void addUser(ModelUser modelUser)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        UserSql.add(db, modelUser);
    }

    public boolean checkLastUserSql()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return LastUserSql.checkTableEmpty(db);
    }

    public ModelUser getLastUserSql()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return LastUserSql.getLastUser(db);
    }

    public void setLastUserSql(ModelUser modelUser)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        LastUserSql.add(db, modelUser);
    }

    public void deleteLastUserTable()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        LastUserSql.deleteLastUserTable(db);
    }

    public void deleteEventTable()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        EventSql.deleteEventTable(db);
    }

    public SQLiteDatabase getReadbleDB()
    {
        return dbHelper.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDB()
    {
        return dbHelper.getWritableDatabase();
    }

    class MyDBHelper extends SQLiteOpenHelper
    {

        public MyDBHelper(Context context)
        {
            super(context, "database.db", null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            //create the DB schema
            EventSql.create(db);
            LastUpdateSql.create(db);
            UserSql.create(db);
            EventsUserSql.create(db);
            LastUserSql.create(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            EventSql.drop(db);
            LastUpdateSql.drop(db);
            UserSql.drop(db);
            EventsUserSql.drop(db);
            LastUserSql.drop(db);
            onCreate(db);
        }
    }
}