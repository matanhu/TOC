package com.example.matanhuja.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.matanhuja.finalproject.Model.Model;
import com.example.matanhuja.finalproject.Model.ModelEvent;
import com.example.matanhuja.finalproject.Model.ModelUser;

//Activity for the event detail page
public class DetailsEventActivity extends Activity
{
    ProgressBar progressBar;
    EditText eventName;
    EditText location;
    EditText startTime;
    EditText startDate;
    EditText endTime;
    EditText endDate;
    EditText finishTime;
    EditText creator;
    EditText members;
    EditText alarmTime;
    EditText alarmDate;
    CheckBox reoucurnce;
    Button editButton;
    Button deleteButton;
    ModelUser modelUser;
    ModelEvent modelEvent;
    ImageView imageView;
    ProgressBar imageProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deails_event);

        Intent intent = getIntent();
        int eventId = intent.getExtras().getInt("Event_Id");
        modelUser = (ModelUser)intent.getExtras().getSerializable("modelUser");

        this.progressBar = (ProgressBar) findViewById(R.id.activity_details_ProgressBar);
        this.eventName = (EditText) findViewById(R.id.activity_details_event_name_view);
        this.location = (EditText) findViewById(R.id.activity_details_event_location);
        this.startTime = (EditText) findViewById(R.id.activity_details_event_start_time);
        this.startDate = (EditText) findViewById(R.id.activity_details_event_start_date);
        this.endTime = (EditText) findViewById(R.id.activity_details_event_end_time);
        this.endDate = (EditText) findViewById(R.id.activity_details_event_end_date);
        this.finishTime = (EditText) findViewById(R.id.activity_details_event_finish_time);
        this.creator = (EditText) findViewById(R.id.activity_details_event_creator);
        this.members = (EditText) findViewById(R.id.activity_details_event_members);
        this.alarmTime = (EditText) findViewById(R.id.activity_details_event_alarm_time);
        this.alarmDate = (EditText) findViewById(R.id.activity_details_event_alarm_date);
        this.reoucurnce = (CheckBox) findViewById(R.id.activity_details_event_reoucurnce_checkbox);
        this.reoucurnce.setClickable(false);
        this.editButton = (Button) findViewById(R.id.activity_details_edit_button);
        this.deleteButton = (Button) findViewById(R.id.activity_details_delete_button);
        this.imageView = (ImageView) findViewById(R.id.activity_details_image);
        this.imageProgressBar = (ProgressBar) findViewById(R.id.activity_details_imageProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        Model.getInstance().getEventFromDB(String.valueOf(eventId), new Model.getEventListener()
        {
            @Override
            public void onResult(ModelEvent event)
            {
                modelEvent = event;
                eventName.setText(modelEvent.getEventName());
                location.setText(modelEvent.getLocation());
                startTime.setText(modelEvent.getStartTime());
                startDate.setText(modelEvent.getStartDate());
                endTime.setText(modelEvent.getEndTime());
                endDate.setText(modelEvent.getEndDate());
                finishTime.setText(modelEvent.getFinishTime());
                creator.setText(modelEvent.getCreator());
                members.setText(modelEvent.getMembers());
                alarmTime.setText(modelEvent.getAlarmTime());
                alarmDate.setText(modelEvent.getAlarmDate());
                reoucurnce.setChecked(modelEvent.isReoucurnce());
                if(modelEvent.getImageName() != null)
                {
                    imageProgressBar.setVisibility(View.VISIBLE);
                    Model.getInstance().loadImage(modelEvent.getImageName(),new Model.LoadImageListener()
                    {
                        @Override
                        public void onResult(Bitmap imageBmp)
                        {
                            if (imageView != null)
                            {
                                imageView.setImageBitmap(imageBmp);
                            }
                            imageProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancel()
            {
            }
        });

        this.editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent editIntent = new Intent(getApplicationContext(), EditEventActivity.class);
                editIntent.putExtra("Event_Id", modelEvent.getEventName()); //Send the data from intent to the new activity
                editIntent.putExtra("modelUser", modelUser); //Send the data from intent to the new activity
                startActivityForResult(editIntent, 2);
            }
        });

        this.deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Model.getInstance().deleteEventFromDB(modelEvent, modelUser.getEmail());
                //Refresh list after a change has been made
                setResult(2);
                finish();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //Check if the resultCode is 2 list updated and must refresh
        if (resultCode == 2)
        {
            Intent returnIntent = getIntent();
            setResult(2, returnIntent);
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
            case R.id.menu_details_edit:
                Intent editIntent = new Intent(getApplicationContext(), EditEventActivity.class);
                editIntent.putExtra("Event_Id", modelEvent.getEventName()); //Send the data from intent to the new activity
                editIntent.putExtra("modelUser", modelUser); //Send the data from intent to the new activity
                startActivityForResult(editIntent, 2);
                return true;
            case R.id.menu_details_delete:
                Model.getInstance().deleteEventFromDB(modelEvent, modelUser.getEmail());
                //Change for refresh after a new event is added
                setResult(2);
                finish();
                return true;
            case  R.id.menu_details_back:
                setResult(2);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
