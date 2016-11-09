package com.example.matanhuja.finalproject.Sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.matanhuja.finalproject.Model.ModelEvent;

import java.util.LinkedList;
import java.util.List;

public class EventSql
{
    private static final String EVENTS_TABLE = "events";
    private static final String USER_EMAIL = "userEmail";
    private static final String EVENT_ID = "id";
    private static final String EVENT_START_TIME = "startTime";
    private static final String EVENT_START_DATE = "startDate";
    private static final String EVENT_END_TIME = "endTime";
    private static final String EVENT_END_DATE = "endDate";
    private static final String EVENT_ALARM_TIME = "alarmTime";
    private static final String EVENT_ALARM_DATE = "alarmDate";
    private static final String EVENT_NAME = "eventName";
    private static final String EVENT_FINISH_TIME = "finishTime";
    private static final String EVENT_LOCATION = "location";
    private static final String EVENT_CREATOR = "creator";
    private static final String EVENT_MEMBERS = "members";
    private static final String EVENT_REOUCURNCE = "reoucurnce";
    private static final String EVENT_IMAGE_NAME = "imageName";

    public static void add(SQLiteDatabase db, ModelEvent event, String email)
    {
        ContentValues values = new ContentValues();
        values.put(USER_EMAIL, email);
        values.put(EVENT_ID, event.getEventId());
        values.put(EVENT_START_TIME, event.getStartTime());
        values.put(EVENT_START_DATE, event.getStartDate());
        values.put(EVENT_END_TIME, event.getEndTime());
        values.put(EVENT_END_DATE, event.getEndDate());
        values.put(EVENT_ALARM_TIME, event.getAlarmTime());
        values.put(EVENT_ALARM_DATE, event.getAlarmDate());
        values.put(EVENT_NAME, event.getEventName());
        values.put(EVENT_FINISH_TIME, event.getFinishTime());
        values.put(EVENT_LOCATION, event.getLocation());
        values.put(EVENT_CREATOR, event.getCreator());
        values.put(EVENT_MEMBERS, event.getMembers());
        values.put(EVENT_IMAGE_NAME, event.getImageName());
        if (event.isReoucurnce())
        {
            values.put(EVENT_REOUCURNCE, "YES");
        }
        else
        {
            values.put(EVENT_REOUCURNCE, "NO");
        }
        db.insertWithOnConflict(EVENTS_TABLE, EVENT_ID, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void update(SQLiteDatabase db, ModelEvent event)
    {
        ContentValues values = new ContentValues();
        values.put(EVENT_START_TIME, event.getStartTime());
        values.put(EVENT_START_DATE, event.getStartDate());
        values.put(EVENT_END_TIME, event.getEndTime());
        values.put(EVENT_END_DATE, event.getEndDate());
        values.put(EVENT_ALARM_TIME, event.getAlarmTime());
        values.put(EVENT_ALARM_DATE, event.getAlarmDate());
        values.put(EVENT_NAME, event.getEventName());
        values.put(EVENT_FINISH_TIME, event.getFinishTime());
        values.put(EVENT_LOCATION, event.getLocation());
        values.put(EVENT_CREATOR, event.getCreator());
        values.put(EVENT_MEMBERS, event.getMembers());
        values.put(EVENT_IMAGE_NAME, event.getImageName());
        if (event.isReoucurnce())
        {
            values.put(EVENT_REOUCURNCE, "YES");
        }
        else
        {
            values.put(EVENT_REOUCURNCE, "NO");
        }
        db.update(EVENTS_TABLE, values, EVENT_ID + "=" + event.getEventId(), null);
    }

    public static ModelEvent getEvent(SQLiteDatabase db, String eventId)
    {
        String[] params = new String[1];
        params[0] = eventId;
        Cursor cursor = db.query(EVENTS_TABLE, null, EVENT_ID + " = ?", params, null, null, null);

        if (cursor.moveToFirst())
        {
            int idIndex = cursor.getColumnIndex(EVENT_ID);
            int startTimeIndex = cursor.getColumnIndex(EVENT_START_TIME);
            int startDateIndex = cursor.getColumnIndex(EVENT_START_DATE);
            int endTimeIndex = cursor.getColumnIndex(EVENT_END_TIME);
            int endDateIndex = cursor.getColumnIndex(EVENT_END_DATE);
            int alarmTimeIndex = cursor.getColumnIndex(EVENT_ALARM_TIME);
            int alarmDateIndex = cursor.getColumnIndex(EVENT_ALARM_DATE);
            int nameIndex = cursor.getColumnIndex(EVENT_NAME);
            int finishTimeIndex = cursor.getColumnIndex(EVENT_FINISH_TIME);
            int locationIndex = cursor.getColumnIndex(EVENT_LOCATION);
            int creatorIndex = cursor.getColumnIndex(EVENT_CREATOR);
            int membersIndex = cursor.getColumnIndex(EVENT_MEMBERS);
            int reoucurnceIndex = cursor.getColumnIndex(EVENT_REOUCURNCE);
            int imageNameIndex = cursor.getColumnIndex(EVENT_IMAGE_NAME);

            String id = cursor.getString(idIndex);
            String startTime = cursor.getString(startTimeIndex);
            String startDate = cursor.getString(startDateIndex);
            String endTime = cursor.getString(endTimeIndex);
            String endDate = cursor.getString(endDateIndex);
            String alarmTime = cursor.getString(alarmTimeIndex);
            String alarmDate = cursor.getString(alarmDateIndex);
            String eventName = cursor.getString(nameIndex);
            String finishTime = cursor.getString(finishTimeIndex);
            String location = cursor.getString(locationIndex);
            String creator = cursor.getString(creatorIndex);
            String members = cursor.getString(membersIndex);
            String reoucurnce = cursor.getString(reoucurnceIndex);
            String imageName = cursor.getString(imageNameIndex);
            boolean isChecked = true;
            if (reoucurnce.equals("NO"))
            {
                isChecked = false;
            }
            ModelEvent event = new ModelEvent(Integer.parseInt(id), eventName, location, startTime, startDate, endTime, endDate, finishTime, creator, members, alarmTime, alarmDate, isChecked, imageName);
            return event;
        }
        return null;
    }

    public static List<ModelEvent> getEvents(SQLiteDatabase db, String email)
    {
        String[] params = new String[1];
        params[0] = email;
        Cursor cursor = db.query(EVENTS_TABLE, null, USER_EMAIL + " = ?", params, null, null, null);

        List<ModelEvent> list = new LinkedList<ModelEvent>();
        if (cursor.moveToFirst())
        {
            int idIndex = cursor.getColumnIndex(EVENT_ID);
            int startTimeIndex = cursor.getColumnIndex(EVENT_START_TIME);
            int startDateIndex = cursor.getColumnIndex(EVENT_START_DATE);
            int endTimeIndex = cursor.getColumnIndex(EVENT_END_TIME);
            int endDateIndex = cursor.getColumnIndex(EVENT_END_DATE);
            int alarmTimeIndex = cursor.getColumnIndex(EVENT_ALARM_TIME);
            int alarmDateIndex = cursor.getColumnIndex(EVENT_ALARM_DATE);
            int nameIndex = cursor.getColumnIndex(EVENT_NAME);
            int finishTimeIndex = cursor.getColumnIndex(EVENT_FINISH_TIME);
            int locationIndex = cursor.getColumnIndex(EVENT_LOCATION);
            int creatorIndex = cursor.getColumnIndex(EVENT_CREATOR);
            int membersIndex = cursor.getColumnIndex(EVENT_MEMBERS);
            int reoucurnceIndex = cursor.getColumnIndex(EVENT_REOUCURNCE);
            int imageNameIndex = cursor.getColumnIndex(EVENT_IMAGE_NAME);
            do
            {
                String id = cursor.getString(idIndex);
                String startTime = cursor.getString(startTimeIndex);
                String startDate = cursor.getString(startDateIndex);
                String endTime = cursor.getString(endTimeIndex);
                String endDate = cursor.getString(endDateIndex);
                String alarmTime = cursor.getString(alarmTimeIndex);
                String alarmDate = cursor.getString(alarmDateIndex);
                String eventName = cursor.getString(nameIndex);
                String finishTime = cursor.getString(finishTimeIndex);
                String location = cursor.getString(locationIndex);
                String creator = cursor.getString(creatorIndex);
                String members = cursor.getString(membersIndex);
                String reoucurnce = cursor.getString(reoucurnceIndex);
                String imageName = cursor.getString(imageNameIndex);
                boolean isChecked = true;
                if (reoucurnce.equals("NO"))
                {
                    isChecked = false;
                }
                ModelEvent event = new ModelEvent(Integer.parseInt(id), eventName, location, startTime, startDate, endTime, endDate, finishTime, creator, members, alarmTime, alarmDate, isChecked, imageName);
                list.add(event);
            }
            while (cursor.moveToNext());
        }
        return list;
    }

    public static void delete(SQLiteDatabase db, ModelEvent modelEvent)
    {
        String[] params = new String[1];
        params[0] = String.valueOf(modelEvent.getEventId());
        db.delete(EVENTS_TABLE, EVENT_ID + " = ?", params);
    }

    public static void create(SQLiteDatabase db)
    {
        db.execSQL("create table " +
                EVENTS_TABLE + " (" +
                USER_EMAIL + " TEXT, " +
                EVENT_ID + " TEXT PRIMARY KEY," +
                EVENT_START_TIME + " TEXT," +
                EVENT_START_DATE + " TEXT," +
                EVENT_END_TIME + " TEXT," +
                EVENT_END_DATE + " TEXT," +
                EVENT_ALARM_TIME + " TEXT," +
                EVENT_ALARM_DATE + " TEXT," +
                EVENT_NAME + " TEXT," +
                EVENT_FINISH_TIME + " TEXT," +
                EVENT_LOCATION + " TEXT," +
                EVENT_CREATOR + " TEXT," +
                EVENT_MEMBERS + " TEXT," +
                EVENT_IMAGE_NAME + " TEXT," +
                EVENT_REOUCURNCE + " TEXT);");
    }

    public static String getLastUpdateDate(SQLiteDatabase db)
    {
        return LastUpdateSql.getLastUpdate(db,EVENTS_TABLE);
    }

    public static void setLastUpdateDate(SQLiteDatabase db, String date)
    {
        LastUpdateSql.setLastUpdate(db,EVENTS_TABLE, date);
    }

    public static void deleteEventTable(SQLiteDatabase db)
    {
        db.delete(EVENTS_TABLE, null, null);
    }

    public static void drop(SQLiteDatabase db)
    {
        db.execSQL("drop table " + EVENTS_TABLE);
    }
}