package com.example.matanhuja.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.matanhuja.finalproject.DatePicker.DateEditText;
import com.example.matanhuja.finalproject.Model.Model;
import com.example.matanhuja.finalproject.Model.ModelEvent;
import com.example.matanhuja.finalproject.Model.ModelUser;
import com.example.matanhuja.finalproject.TimePicker.TimeEditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Activity for the event new page
public class NewEventActivity extends Activity
{
    EditText eventName;
    EditText location;
    DateEditText startDate;
    TimeEditText startTime;
    DateEditText endDate;
    TimeEditText endTime;
    DateEditText finishTime;
    EditText members;
    DateEditText alarmDate;
    TimeEditText alarmTime;
    CheckBox reoucurnce;
    Button saveButton;
    Button cancelButton;
    ModelEvent modelEvent;
    ModelUser modelUser;
    ImageView imageView;
    String imageFileName = null;
    Bitmap imageBitmap = null;
    String userChoosenTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        Intent intent = getIntent();
        String startTimestr = intent.getExtras().getString("Date");
        modelUser = (ModelUser)intent.getExtras().getSerializable("modelUser");

        this.eventName = (EditText) findViewById(R.id.activity_new_event_name_view);
        this.location = (EditText) findViewById(R.id.activity_new_event_location_edit);
        this.startDate = (DateEditText) findViewById(R.id.activity_new_event_start_date_edit);
        this.startTime = (TimeEditText) findViewById(R.id.activity_new_event_start_time_edit);
        this.endDate = (DateEditText) findViewById(R.id.activity_new_event_end_date_edit);
        this.endTime = (TimeEditText) findViewById(R.id.activity_new_event_end_time_edit);
        this.finishTime = (DateEditText) findViewById(R.id.activity_new_event_finish_time_edit);
        this.members = (EditText) findViewById(R.id.activity_new_event_members_edit);
        this.alarmDate = (DateEditText) findViewById(R.id.activity_new_event_alarm_date_edit);
        this.alarmTime = (TimeEditText) findViewById(R.id.activity_new_event_alarm_time_edit);
        this.reoucurnce = (CheckBox) findViewById(R.id.activity_new_event_reoucurnce_checkbox);
        this.saveButton = (Button) findViewById(R.id.activity_new_event_save_btn);
        this.cancelButton = (Button) findViewById(R.id.activity_new_event_cancel_btn);
        this.startDate.setText(startTimestr);
        imageView = (ImageView) findViewById(R.id.activity_new_event_image);


        startDate.setOnDateSetListener(new DateEditText.OnDateSetListener()
        {
            @Override
            public void dateSet(int year, int month, int day) {
                Log.d("TAG", "activity got the date:" + day + "/" + month + "/" + year);
            }
        });

        endDate.setOnDateSetListener(new DateEditText.OnDateSetListener()
        {
            @Override
            public void dateSet(int year, int month, int day)
            {
                Log.d("TAG", "activity got the date:" + day + "/" + month + "/" + year);
            }
        });

        alarmDate.setOnDateSetListener(new DateEditText.OnDateSetListener()
        {
            @Override
            public void dateSet(int year, int month, int day)
            {
                Log.d("TAG", "activity got the date:" + day + "/" + month + "/" + year);
            }
        });

        finishTime.setOnDateSetListener(new DateEditText.OnDateSetListener()
        {
            @Override
            public void dateSet(int year, int month, int day)
            {
                Log.d("TAG", "activity got the date:" + day + "/" + month + "/" + year);
            }
        });

        startTime.setOnClickListener(new TimeEditText.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("TAG", v.toString());
            }
        });

        endTime.setOnClickListener(new TimeEditText.OnClickListener()
        {
        @Override
            public void onClick(View v)
        {
                Log.d("TAG", v.toString());
            }
        });

        alarmTime.setOnClickListener(new TimeEditText.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("TAG", v.toString());
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, resultIntent);
                finish();
            }
        });

        //Save event ation - sets all the attirbutes populated
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                modelEvent = new ModelEvent();
                modelEvent.setEventName(eventName.getText().toString());
                modelEvent.setLocation(location.getText().toString());
                modelEvent.setStartTime(startTime.getText().toString());
                modelEvent.setStartDate(startDate.getText().toString());
                modelEvent.setEndTime(endTime.getText().toString());
                modelEvent.setEndDate(endDate.getText().toString());
                modelEvent.setFinishTime(finishTime.getText().toString());
                modelEvent.setMembers(members.getText().toString());
                modelEvent.setAlarmTime(alarmTime.getText().toString());
                modelEvent.setAlarmDate(alarmDate.getText().toString());
                modelEvent.setReoucurnce(reoucurnce.isChecked());
                modelEvent.setEventId();
                modelEvent.setCreator(modelUser.getEmail());
                //Check if a there is an image in the event
                if(imageBitmap != null)
                {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    imageFileName = String.valueOf(modelEvent.getEventId()) + timeStamp + ".jpg";
                    modelEvent.setImageName(imageFileName);
                    Model.getInstance().saveImage(imageBitmap, imageFileName);  //Save image to cloudinary
                }
                Model.getInstance().addEventToDB(modelEvent, modelUser.getEmail()); //Add event to local database
                Model.getInstance().add(modelEvent);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("Event_Id", modelEvent.getEventId()); //Send the data from intent to the new activity
                setResult(2, resultIntent); //Change for refresh list after a new event is added
                finish();
            }
        });

        //Image selector
        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectImage();
            }
        });
    }

    //Take a picture for event
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void takingPicture()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
        if(requestCode==GET_FROM_GALLERY && resultCode == RESULT_OK)
        {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try
            {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                int targetWidth  = imageView.getWidth() - 15; //change this to control the size
                int targetHeight = imageView.getHeight() - 15 ;
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, targetWidth, targetHeight, true);
                imageView.setImageBitmap(resizedBitmap);
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new, menu);
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
            case R.id.menu_new_save:
                modelEvent = new ModelEvent();
                modelEvent.setEventName(eventName.getText().toString());
                modelEvent.setLocation(location.getText().toString());
                modelEvent.setStartTime(startTime.getText().toString());
                modelEvent.setStartDate(startDate.getText().toString());
                modelEvent.setEndTime(endTime.getText().toString());
                modelEvent.setEndDate(endDate.getText().toString());
                modelEvent.setFinishTime(finishTime.getText().toString());
                modelEvent.setMembers(members.getText().toString());
                modelEvent.setAlarmTime(alarmTime.getText().toString());
                modelEvent.setAlarmDate(alarmDate.getText().toString());
                modelEvent.setReoucurnce(reoucurnce.isChecked());
                modelEvent.setEventId();
                modelEvent.setCreator(modelUser.getEmail());
                if(imageBitmap != null)
                {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    imageFileName = String.valueOf(modelEvent.getEventId()) + timeStamp + ".jpg";
                    modelEvent.setImageName(imageFileName);
                    Model.getInstance().saveImage(imageBitmap, imageFileName);
                }
                Model.getInstance().addEventToDB(modelEvent, modelUser.getEmail());
                Model.getInstance().add(modelEvent);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("Event_Id", modelEvent.getEventId()); //send the data from intent to the new activity
                //Matan - Change for Refresh List After Add New Event
                setResult(2, resultIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    static final int GET_FROM_GALLERY = 3;
    private void takePictureFromGallery()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GET_FROM_GALLERY);
    }


    //Select image for event
    private void selectImage()
    {
        final CharSequence[] items = { "צלם תמונה", "בחר תמונה מהגלריה","ביטול"};
        AlertDialog.Builder builder = new AlertDialog.Builder(NewEventActivity.this);
        builder.setTitle("הוספת תמונה");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                boolean result=Utility.checkPermission(NewEventActivity.this);
                if (items[item].equals("צלם תמונה"))
                {
                    userChoosenTask="צלם תמונה";
                    if(result)
                        takingPicture();
                }
                else if (items[item].equals("בחר תמונה מהגלריה"))
                {
                    userChoosenTask="בחר תמונה מהגלריה";
                    if(result)
                        takePictureFromGallery();
                }
                else if (items[item].equals("ביטול"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}
