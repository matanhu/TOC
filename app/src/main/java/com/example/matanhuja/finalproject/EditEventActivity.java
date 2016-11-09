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
import android.widget.ProgressBar;

import com.example.matanhuja.finalproject.DatePicker.DateEditText;
import com.example.matanhuja.finalproject.Model.Model;
import com.example.matanhuja.finalproject.Model.ModelEvent;
import com.example.matanhuja.finalproject.Model.ModelUser;
import com.example.matanhuja.finalproject.TimePicker.TimeEditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Activity for the event edit page
public class EditEventActivity extends Activity
{
    ProgressBar progressBar;
    EditText eventName;
    EditText location;
    TimeEditText startTime;
    DateEditText startDate;
    TimeEditText endTime;
    DateEditText endDate;
    DateEditText finishTime;
    EditText members;
    TimeEditText alarmTime;
    DateEditText alarmDate;
    CheckBox reoucurnce;
    Button saveButton;
    Button cancelButton;
    ModelEvent modelEvent;
    ModelUser modelUser;
    ImageView imageView;
    String imageFileName = null;
    Bitmap imageBitmap = null;
    String oldNameImage = null;
    ProgressBar imageProgressBar;
    String userChoosenTask = null;
    String oldMemebers = null;
    String creator = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        Intent intent = getIntent();
        String eventNameStr = intent.getExtras().getString("Event_Id");
        modelEvent = Model.getInstance().getEvent(eventNameStr);
        modelUser = (ModelUser)intent.getExtras().getSerializable("modelUser");

        this.progressBar = (ProgressBar) findViewById(R.id.activity_edit_ProgressBar);
        this.eventName = (EditText) findViewById(R.id.activity_edit_event_name_view);
        this.location = (EditText) findViewById(R.id.activity_edit_event_location_edit);
        this.startTime = (TimeEditText) findViewById(R.id.activity_edit_event_start_time_edit);
        this.startDate = (DateEditText) findViewById(R.id.activity_edit_event_start_date_edit);
        this.endTime = (TimeEditText) findViewById(R.id.activity_edit_event_end_time_edit);
        this.endDate = (DateEditText) findViewById(R.id.activity_edit_event_end_date_edit);
        this.finishTime = (DateEditText) findViewById(R.id.activity_edit_event_finish_time_edit);
        this.members = (EditText) findViewById(R.id.activity_edit_event_members_edit);
        this.alarmTime = (TimeEditText) findViewById(R.id.activity_edit_event_alarm_time_edit);
        this.alarmDate = (DateEditText) findViewById(R.id.activity_edit_event_alarm_date_edit);
        this.reoucurnce = (CheckBox) findViewById(R.id.activity_edit_event_reoucurnce_checkbox);
        this.saveButton = (Button) findViewById(R.id.activity_edit_event_save_btn);
        this.cancelButton = (Button) findViewById(R.id.activity_edit_event_cancel_btn);
        this.imageView = (ImageView) findViewById(R.id.activity_edit_event_image);
        this.imageProgressBar = (ProgressBar) findViewById(R.id.activity_edit_event_imageProgressBar);
        this.progressBar.setVisibility(View.VISIBLE);
        this.eventName.setText(modelEvent.getEventName());
        this.location.setText(modelEvent.getLocation());
        this.startTime.setText(modelEvent.getStartTime());
        this.startDate.setText(modelEvent.getStartDate());
        this.endTime.setText(modelEvent.getEndTime());
        this.endDate.setText(modelEvent.getEndDate());
        this.finishTime.setText(modelEvent.getFinishTime());
        this.members.setText(modelEvent.getMembers());
        this.oldMemebers = modelEvent.getMembers();
        this.alarmTime.setText(modelEvent.getAlarmTime());
        this.alarmDate.setText(modelEvent.getAlarmDate());
        this.reoucurnce.setChecked(modelEvent.isReoucurnce());
        this.creator = modelEvent.getCreator();

        if(modelEvent.getImageName() != null)
        {
            imageProgressBar.setVisibility(View.VISIBLE);
            oldNameImage = modelEvent.getImageName();
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
        this.progressBar.setVisibility(View.GONE);

        startDate.setOnDateSetListener(new DateEditText.OnDateSetListener()
        {
            @Override
            public void dateSet(int year, int month, int day)
            {
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

        finishTime.setOnDateSetListener(new DateEditText.OnDateSetListener()
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

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
                modelEvent.setCreator(creator);
                if(imageBitmap != null)
                {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()); //Create image unique file name
                    imageFileName = String.valueOf(modelEvent.getEventId()) + timeStamp + ".jpg"; //Set image file name
                    modelEvent.setImageName(imageFileName);
                    Model.getInstance().saveImage(imageBitmap, imageFileName); //Save image to cloudinary
                }
                Model.getInstance().updateEventOnDB(modelEvent, modelUser.getEmail(), oldMemebers);

                Intent resultIntent = getIntent();
                resultIntent.putExtra("Event_Id", modelEvent.getEventName()); //Send the data from intent to the new activity
                setResult(2, resultIntent);
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectImage();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent resultIntent;

        switch(id)
        {
            case R.id.menu_edit_save:
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
                if(imageBitmap != null)
                {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    imageFileName = String.valueOf(modelEvent.getEventId()) + timeStamp + ".jpg";
                    modelEvent.setImageName(imageFileName);
                    Model.getInstance().saveImage(imageBitmap, imageFileName);
                }
                Model.getInstance().updateEventOnDB(modelEvent, modelUser.getEmail(), oldMemebers);

                resultIntent = getIntent();
                resultIntent.putExtra("Event_Id", modelEvent.getEventName()); //Send the data from intent to the new activity
                setResult(2, resultIntent);
                finish();
                return true;
            }
        return super.onOptionsItemSelected(item);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private void takingPicture()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
        if (requestCode == GET_FROM_GALLERY && resultCode == RESULT_OK)
        {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try
            {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                int targetWidth = imageView.getWidth() - 15; //Size control
                int targetHeight = imageView.getHeight() - 15;
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
        final CharSequence[] items = { "צלם תמונה", "בחר תמונה מהגלריה","ביטול" };
        AlertDialog.Builder builder = new AlertDialog.Builder(EditEventActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                boolean result=Utility.checkPermission(EditEventActivity.this);
                if (items[item].equals("צלם תמונה"))
                {
                    userChoosenTask="צלם תמונה";
                    if(result)
                        takingPicture();
                } else if (items[item].equals("בחר תמונה מהגלריה"))
                {
                    userChoosenTask="בחר תמונה מהגלריה";
                    if(result)
                        takePictureFromGallery();
                } else if (items[item].equals("ביטול"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}
