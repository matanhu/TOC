package com.example.matanhuja.finalproject.Model;

import android.content.Context;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class ModelFirebase
{
    Firebase myfirebaseRef;


    ModelFirebase(Context context)
    {
        Firebase.setAndroidContext(context);
        myfirebaseRef = new Firebase("https://androidprojectmo.firebaseIO.com/");
    }

    public void addEventToDB(ModelEvent modelEvent, String emailUser)
    {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = null;
        date =  dateFormatGmt.format(new Date()) .toString();
        modelEvent.setLastUpdated(date);

        String [] members = modelEvent.getMembers().split((";"));
        Firebase eventRef = myfirebaseRef.child("Events").child(Integer.toString(modelEvent.getEventId()));
        eventRef.setValue(modelEvent);
        emailUser = emailUser.replaceAll("\\.","_").trim();
        Firebase currentUser = myfirebaseRef.child("Users").child(emailUser).child("Events").child(Integer.toString(modelEvent.getEventId()));
        currentUser.setValue(modelEvent.getEventId());
        currentUser = myfirebaseRef.child("Users").child(emailUser).child("lastUpdate");
        currentUser.setValue(date);

        for (String member: members)
        {
            if(member!="")
            {
                member = member.replaceAll("\\.", "_").trim();
                Firebase membersRef = myfirebaseRef.child("Users").child(member).child("Events").child(Integer.toString(modelEvent.getEventId()));
                membersRef.setValue(modelEvent.getEventId());
                membersRef = myfirebaseRef.child("Users").child(member).child("lastUpdate");
                membersRef.setValue(date);
            }
        }
    }

    public void updateEventOnDB(ModelEvent modelEvent, String emailUser, String oldMemebers)
    {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = null;
        date =  dateFormatGmt.format(new Date()) .toString();
        modelEvent.setLastUpdated(date);

        Firebase eventRef = myfirebaseRef.child("Events").child(Integer.toString(modelEvent.getEventId()));
        eventRef.setValue(modelEvent);

        String [] oldMemebersArray = oldMemebers.split((";"));
        for (String oldMember: oldMemebersArray)
        {
            if(oldMember!="")
            {
                oldMember = oldMember.replaceAll("\\.", "_").trim();
                Firebase membersRef = myfirebaseRef.child("Users").child(oldMember).child("Events").child(Integer.toString(modelEvent.getEventId()));
                membersRef.removeValue();
                membersRef = myfirebaseRef.child("Users").child(oldMember).child("lastUpdate");
                membersRef.setValue(date);
            }
        }

        String [] members = modelEvent.getMembers().split((";"));
        emailUser = emailUser.replaceAll("\\.", "_").trim();
        Firebase currentUser = myfirebaseRef.child("Users").child(emailUser).child("lastUpdate");
        currentUser.setValue(date);

        for (String member: members)
        {
            if(member!="")
            {
                member = member.replaceAll("\\.", "_").trim();
                Firebase membersRef = myfirebaseRef.child("Users").child(member).child("Events").child(Integer.toString(modelEvent.getEventId()));
                membersRef.setValue(modelEvent.getEventId());
                membersRef = myfirebaseRef.child("Users").child(member).child("lastUpdate");
                membersRef.setValue(date);
            }
        }
        String creatoruser = modelEvent.getCreator().replaceAll("\\.", "_").trim();
        Firebase creatorRef = myfirebaseRef.child("Users").child(creatoruser).child("lastUpdate");
        creatorRef.setValue(date);
    }

    public void deleteEventFromDB(ModelEvent modelEvent, String emailUser)
    {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = null;
        date =  dateFormatGmt.format(new Date()) .toString();
        modelEvent.setLastUpdated(date);
        String [] members = modelEvent.getMembers().split((";"));
        Firebase eventRef = myfirebaseRef.child("Events").child(Integer.toString(modelEvent.getEventId()));
        eventRef.removeValue();
        emailUser = modelEvent.getCreator().replaceAll("\\.","_").trim();
        Firebase currentUser = myfirebaseRef.child("Users").child(emailUser).child("Events").child(Integer.toString(modelEvent.getEventId()));
        currentUser.removeValue();
        currentUser = myfirebaseRef.child("Users").child(emailUser).child("lastUpdate");
        currentUser.setValue(date);

        for (String member: members)
        {
            if(member!="")
            {
                member = member.replaceAll("\\.", "_").trim();
                Firebase membersRef = myfirebaseRef.child("Users").child(member).child("Events").child(Integer.toString(modelEvent.getEventId()));
                membersRef.removeValue();
                membersRef = myfirebaseRef.child("Users").child(member).child("lastUpdate");
                membersRef.setValue(date);
            }
        }

    }

    public void readEventFromDB(final Model.getEventListener listener, String eventId)
    {
        Firebase eventRef = myfirebaseRef.child("Events").child(eventId);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                ModelEvent event = snapshot.getValue(ModelEvent.class);
                listener.onResult(event);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError)
            {
                System.out.println("The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });

    }

    public void readLastUpdateUserFromDB(String user, final Model.getLastUpdateUserListener listener)
    {
        String emailUser = user.replaceAll("\\.","_").trim();
        Firebase eventRef = myfirebaseRef.child("Users").child(emailUser).child("lastUpdate");
        eventRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot snapshot)
            {
                String lastUpdate = snapshot.getValue(String.class);
                Log.d("TAG", "LAST UPDATE: " + lastUpdate);
                listener.onResult(lastUpdate);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError)
            {
                System.out.println("The read failed: " + firebaseError.getMessage());
                listener.onCancel();
            }
        });

    }

    public void readAllEventsFromDB(String userEmail, final Model.getEventsListener listener)
    {
        String keyEmail = userEmail.replaceAll("\\.", "_");
        Firebase eventRef = myfirebaseRef.child("Users").child(keyEmail).child("Events");
        final ArrayList<String> events = new ArrayList<>();
        eventRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
                @Override
                public void onDataChange(DataSnapshot snapshot)
                {
                    final List<ModelEvent> eventList = new LinkedList<ModelEvent>();
                    for (DataSnapshot eventSnapshot : snapshot.getChildren())
                    {
                        events.add(eventSnapshot.getValue(String.class));
                    }

                        Firebase eventRef = myfirebaseRef.child("Events");
                        eventRef.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot snapshot)
                            {
                                for (String eventItr:events)
                                {
                                    eventList.add(snapshot.child(eventItr).getValue(ModelEvent.class));
                                }

                                listener.onResult(eventList);
                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                System.out.println("The read failed: " + firebaseError.getMessage());
                            }
                        });
                }

                @Override
                public void onCancelled (FirebaseError firebaseError)
                {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                    listener.onCancel();
                }
        });
    }

    public void createUser(final ModelUser modelUser, final Model.createUserListener listener)
    {
        final String email = modelUser.getEmail().trim();
        final String keyMail = email.replaceAll("\\.","_").trim();
        myfirebaseRef.createUser(modelUser.getEmail(), modelUser.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>()
        {
            @Override
            public void onSuccess(Map<String, Object> result)
            {
                Log.d("TAG", "Create User Successed and the UID is: " + result.get("uid"));
                Firebase emailRef = myfirebaseRef.child("Users").child(keyMail).child("email");
                emailRef.setValue(email);
                Firebase uidRef = myfirebaseRef.child("Users").child(keyMail).child("uid");
                uidRef.setValue(result.get("uid"));
                listener.onSuccess(result.get("uid").toString());
            }

            @Override
            public void onError(FirebaseError firebaseError)
            {
                Log.d("TAG", "Create User Faild");
                listener.onError();
            }
        });
    }

    public void loginUser(ModelUser modelUser, final Model.loginUserListener listener)
    {
        myfirebaseRef.authWithPassword(modelUser.getEmail(), modelUser.getPassword(), new Firebase.AuthResultHandler()
        {
            @Override
            public void onAuthenticated(AuthData authData)
            {
                Log.d("TAG", "Login User Successed and the UID is: " + authData.getUid());
                listener.onAuthenticated(authData.getUid().toString());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError)
            {
                Log.d("TAG", "Login User Failed: " + firebaseError.getDetails());
                listener.onAuthenticationError();
            }
        });
    }
}

