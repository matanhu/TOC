package com.example.matanhuja.finalproject.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.matanhuja.finalproject.MyApplication;
import com.example.matanhuja.finalproject.Sql.EventSql;
import com.example.matanhuja.finalproject.Sql.EventsUserSql;
import com.example.matanhuja.finalproject.Sql.ModelSql;
import com.example.matanhuja.finalproject.Sql.UserSql;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class Model
{
    List<ModelEvent> data = new LinkedList<ModelEvent>();
    Context context;
    ModelFirebase modelFirebase;
    ModelSql modelSql;
    ModelCloudinary modelCloudinary;
    String lastUpdateFireBase;

    private final static Model instance = new Model();

    private Model()
    {
        this.context = MyApplication.getAppContext();
        this.modelFirebase = new ModelFirebase(this.context);
        this.modelSql = new ModelSql(this.context);
        this.modelCloudinary = new ModelCloudinary();
        init();

    }

    public static Model instance()
    {
        return instance;
    }

    public static Model getInstance()
    {
        return instance;
    }

    public void init()
    {
    }

    public void createUser(ModelUser modelUser, createUserListener listener)
    {
        modelFirebase.createUser(modelUser, listener);
        modelSql.addUser(modelUser);
    }

    public void loginUser(ModelUser modelUser, loginUserListener listener)
    {
        modelFirebase.loginUser(modelUser, listener);
    }

    public void getEventFromDB(String eventId, getEventListener listener)
    {
        modelFirebase.readEventFromDB(listener, eventId);
    }

    public void getLastUpdateUser(String emailUser, getLastUpdateUserListener listener)
    {
        modelFirebase.readLastUpdateUserFromDB(emailUser, listener);
    }

    public void getAllEventsFromDB(final String userEmail, final getEventsListener listener)
    {
        final String lastUpdateLocal = UserSql.getLastUpdateDate(modelSql.getReadbleDB(), userEmail);
        getLastUpdateUser(userEmail, new getLastUpdateUserListener() {
            @Override
            public void onResult(String lastUpdate) {
                lastUpdateFireBase = lastUpdate;
                if (lastUpdateLocal == null || lastUpdateFireBase.compareTo(lastUpdateLocal) > 0) {
                    List<ModelEvent> eventsFromLocalDb = modelSql.getEvents(userEmail);
                    for (ModelEvent eventFromDb:eventsFromLocalDb) {
                        modelSql.delete(eventFromDb);
                    }
                    modelFirebase.readAllEventsFromDB(userEmail, new getEventsListener() {
                        @Override
                        public void onResult(List<ModelEvent> events) {
                            if (events != null && events.size() > 0) {
                                //modelSql.deleteEventTable();
                                for (ModelEvent event : events) {
                                    EventSql.add(modelSql.getWritableDB(), event, userEmail);
                                    EventsUserSql.add(modelSql.getWritableDB(), userEmail, String.valueOf(event.getEventId()));
                                    Log.d("TAG", "updating: " + event.toString());
                                }
                            }
                            List<ModelEvent> res = EventSql.getEvents(modelSql.getReadbleDB(), userEmail);
                            UserSql.setLastUpdateDate(modelSql.getWritableDB(), lastUpdateFireBase, userEmail);
                            data = res;
                            listener.onResult(res);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });

                } else {
                    List<String> eventsIds = modelSql.getUserEventsId(userEmail);
                    List<ModelEvent> res = new LinkedList<ModelEvent>();
                    for (String eventId : eventsIds) {
                        res.add(modelSql.getEvent(eventId));
                    }
                    data = res;
                    listener.onResult(res);
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void addEventToDB(ModelEvent modelEvent,String emailUser)
    {
        modelFirebase.addEventToDB(modelEvent, emailUser);
        modelSql.addEvent(modelEvent, emailUser);
    }

    public void saveImage(final Bitmap imageBitmap, final String imageName) {
        saveImageToFile(imageBitmap,imageName); //Synchronously save image locally
        Thread d = new Thread(new Runnable()
        {  //Asynchronously save image to parse
            @Override
            public void run()
            {
                modelCloudinary.saveImage(imageBitmap,imageName);
            }
        });
        d.start();
    }

    public void loadImage(final String imageName, final LoadImageListener listener)
    {
        AsyncTask<String,String,Bitmap> task = new AsyncTask<String, String, Bitmap >()
        {
            @Override
            protected Bitmap doInBackground(String... params)
            {
                Bitmap bmp = loadImageFromFile(imageName);//First try to find the image on the device
                //If image not found - try downloading it from parse
                if (bmp == null)
                {
                    bmp = modelCloudinary.loadImage(imageName);
                    if (bmp != null) saveImageToFile(bmp,imageName);//Save the image locally for next time
                }
                else
                {
                    int targetWidth  = 252 - 15; //Change this to control the size
                    int targetHeight = 252 - 15 ;
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, targetWidth, targetHeight, true);
                    return resizedBitmap;
                }
                return bmp;
            }
            @Override
            protected void onPostExecute(Bitmap result)
            {
                listener.onResult(result);
            }
        };
        task.execute();
    }

    private Bitmap loadImageFromFile(String imageFileName)
    {
        String str = null;
        Bitmap bitmap = null;
        try
        {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void updateEventOnDB(ModelEvent modelEvent, String emailUser, String oldMemebers)
    {
        modelFirebase.updateEventOnDB(modelEvent, emailUser, oldMemebers);
        modelSql.updateEvent(modelEvent);
    }

    public void deleteEventFromDB(ModelEvent modelEvent, String emailUser)
    {
        modelFirebase.deleteEventFromDB(modelEvent, emailUser);
        modelSql.delete(modelEvent);
        for(ModelEvent event : data)
        {
            if (event.getEventId() == modelEvent.getEventId())
            {
                data.remove(event);
                break;
            }
        }
    }

    public ModelEvent getEvent(String eventName)
    {
        for (ModelEvent modelEvent :data)
        {
            if(modelEvent.getEventName().equals(eventName))
            {
                return modelEvent;
            }
        }
        return null;
    }

    public void add(ModelEvent modelEvent)
    {
        data.add(modelEvent);
    }

    public void delete(ModelEvent modelEvent)
    {
        data.remove(modelEvent);
    }

    public List<ModelEvent> getEvents()
    {
        return data;
    }

    public boolean checkLastUserSql()
    {
        return modelSql.checkLastUserSql();
    }

    public ModelUser getLastUserSql()
    {
        return modelSql.getLastUserSql();
    }

    public void setLastUserSql(ModelUser modelUser)
    {
        modelSql.setLastUserSql(modelUser);
    }

    public void deleteLastUserSql()
    {
        modelSql.deleteLastUserTable();
    }

    public interface getEventListener
    {
        public void onResult(ModelEvent event);
        public void onCancel();
    }

    public interface getEventsListener
    {
        public void onResult(List<ModelEvent> events);
        public void onCancel();
    }

    public interface createUserListener
    {
        public void onSuccess(String uid);
        public void onError();
    }

    public interface loginUserListener
    {
        public void onAuthenticated(String uid);
        public void onAuthenticationError();
    }

    public interface getLastUpdateUserListener
    {
        public void onResult(String lastUpdate);
        public void onCancel();
    }

    public interface getEventByIdListener
    {
        public void onResult(ModelEvent event);
        public void onCancel();
    }

    public interface LoadImageListener
    {
        public void onResult(Bitmap imageBmp);
    }

    private void saveImageToFile(Bitmap imageBitmap, String imageFileName)
    {
        FileOutputStream fos;
        OutputStream out = null;
        try
        {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!dir.exists())
            {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();

            out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //Add the picture to the gallery so we dont need to manage the cache size
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(imageFile);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
            Log.d("tag","add image to cache: " + imageFileName);

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}