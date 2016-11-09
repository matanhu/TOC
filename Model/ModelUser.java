package com.example.matanhuja.finalproject.Model;

import java.io.Serializable;

public class ModelUser implements Serializable
{
    String email;
    String password;
    String uid;
    String lastUpdated;

    public ModelUser(){};

    public ModelUser(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public void modelUserToLocalDb(String email, String uid)
    {
        this.email = email;
        this.uid = uid;
    }

    public ModelUser(String email, String password, String uid)
    {
        this.email = email;
        this.password = password;
        this.uid = uid;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }
}
