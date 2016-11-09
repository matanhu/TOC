package com.example.matanhuja.finalproject.Model;

import java.util.Date;

public class ModelEvent
{
    static int eventCounter = 000;
    int eventId;
    String eventName;
    String location;
    String startDate;
    String startTime;
    String endDate;
    String endTime;
    String finishTime;
    String creator;
    String members;
    String alarmDate;
    String alarmTime;
    boolean reoucurnce;
    String lastUpdated;
    String imageName;

    public ModelEvent()
    {
    }

    public ModelEvent(String _eventName, String _location, String _startTime, String _startDate, String _endTime, String _endDate, String _finishTime, String _creator, String _members, String _alarmTime, String _alarmDate, boolean _reoucurnce)
    {
        this.eventName = _eventName;
        this.eventId = eventName.hashCode();
        this.location = _location;
        this.startTime = _startTime;
        this.startDate = _startDate;
        this.endTime = _endTime;
        this.endDate = _endDate;
        this.finishTime = _finishTime;
        this.creator = _creator;
        this.members = _members;
        this.alarmTime = _alarmTime;
        this.alarmDate = _alarmDate;
        this.reoucurnce = _reoucurnce;
    }

    public ModelEvent(int _id, String _eventName, String _location, String _startTime, String _startDate,  String _endTime, String _endDate, String _finishTime, String _creator, String _members, String _alarmTime, String _alarmDate, boolean _reoucurnce, String _imageName)
    {
        this.eventName = _eventName;
        this.eventId = _id;
        this.location = _location;
        this.startTime = _startTime;
        this.startDate = _startDate;
        this.endTime = _endTime;
        this.endDate = _endDate;
        this.finishTime = _finishTime;
        this.creator = _creator;
        this.members = _members;
        this.alarmTime = _alarmTime;
        this.alarmDate = _alarmDate;
        this.reoucurnce = _reoucurnce;
        this.imageName = _imageName;
    }

    public static void setEventCounter(int eventCounter)
    {
        ModelEvent.eventCounter = eventCounter;
    }

    public void setEventId(int eventId)
    {
        this.eventId = eventId;
    }

    public void setEventId()
    {
        this.eventId = (this.eventName + (new Date().toString())).hashCode();
    }

    public int getEventId()
    {
        return this.eventId;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getEventName()
    {
        return eventName;
    }

    public void setEventName(String eventName)
    {
        this.eventName = eventName;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getFinishTime()
    {
        return finishTime;
    }

    public void setFinishTime(String finishTime)
    {
        this.finishTime = finishTime;
    }

    public String getCreator()
    {
        return this.creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public String getMembers()
    {
        return members;
    }

    public void setMembers(String members)
    {
        this.members = members;
    }

    public String getAlarmDate()
    {
        return alarmDate;
    }

    public void setAlarmDate(String alarmDate)
    {
        this.alarmDate = alarmDate;
    }

    public boolean isReoucurnce()
    {
        return reoucurnce;
    }

    public void setReoucurnce(boolean reoucurnce)
    {
        this.reoucurnce = reoucurnce;
    }

    public String getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }

    public String getImageName()
    {
        return this.imageName;
    }

    public String getAlarmTime()
    {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime)
    {
        this.alarmTime = alarmTime;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
}
