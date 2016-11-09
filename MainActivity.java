package com.example.matanhuja.finalproject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.matanhuja.finalproject.Calendar.CalendarViewFragment;
import com.example.matanhuja.finalproject.List.ListEventsFragment;
import com.example.matanhuja.finalproject.Model.Model;
import com.example.matanhuja.finalproject.Model.ModelEvent;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity
{
    CalendarViewFragment calendarView;
    ListEventsFragment listEvents;
    ProgressBar progressBar;
    Serializable modelUser;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ModelEvent.setEventCounter(0);

        this.progressBar = (ProgressBar) findViewById(R.id.mainProgressBar);

        Intent intent = getIntent();
        modelUser = intent.getExtras().getSerializable("modelUser");
        listEvents = new ListEventsFragment();

        calendarView = new CalendarViewFragment();

        //Passing ModelUser Object To List Fragment
        Bundle listEventArgs = new Bundle();
        listEventArgs.putSerializable("modelUser", modelUser);
        listEvents.setArguments(listEventArgs);

        //Passing ModelUser Object To Calendar Fragment
        Bundle calendarArgs = new Bundle();
        calendarArgs.putSerializable("modelUser", modelUser);
        calendarView.setArguments(calendarArgs);

        transaction = getFragmentManager().beginTransaction();

        //Add the canendarView and the listEvents fragments
        transaction.add(R.id.main_frag_container_top, calendarView, "y");
        transaction.add(R.id.main_frag_container_bottom, listEvents, "y");
        transaction.show(calendarView);
        transaction.show(listEvents);
        transaction.commit();
    }

    //Made for updating the fragment after a update of addition
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2)
        {
            listEvents.onActivityResult(requestCode,resultCode,data);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.menu_main_add:
                String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                Intent newEventIntent = new Intent(getApplicationContext(),NewEventActivity.class);
                newEventIntent.putExtra("Date", date); //send the data from intent to the new activity
                newEventIntent.putExtra("modelUser", modelUser);
                startActivityForResult(newEventIntent,1);
                return true;
            case R.id.menu_main_exit:
                Intent resultExit = new Intent();
                setResult(1, resultExit);
                finish();
                return true;
            case R.id.menu_main_loged_out:
                Model.getInstance().deleteLastUserSql();
                Intent resultLogedOut = new Intent();
                setResult(2, resultLogedOut);
                finish();
                return true;
            case R.id.menu_main_refresh:
                FragmentTransaction transactionRefresh = getFragmentManager().beginTransaction();
                transactionRefresh.detach(listEvents);
                transactionRefresh.attach(listEvents);
                transactionRefresh.commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startProgressbar()
    {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    public void stopProgressbar()
    {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed()
    {
        Intent resultIntent = new Intent();
        setResult(1, resultIntent);
        finish();
    }
}
