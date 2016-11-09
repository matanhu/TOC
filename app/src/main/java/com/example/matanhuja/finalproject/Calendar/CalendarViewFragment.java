package com.example.matanhuja.finalproject.Calendar;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.matanhuja.finalproject.Model.ModelUser;
import com.example.matanhuja.finalproject.NewEventActivity;
import com.example.matanhuja.finalproject.R;

public class CalendarViewFragment extends Fragment
{
    CalendarView calendarView;
    ModelUser modelUser;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_calendar_view, container, false);
        //Get ModelUser To Calendar Fragment
        Bundle args = getArguments();
        modelUser = (ModelUser)args.getSerializable("modelUser");
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day)
            {
                month +=1;
                Intent newEventIntent = new Intent(getActivity(), NewEventActivity.class);
                newEventIntent.putExtra("Date", day + "/" + month + "/" + year); //send the data from intent to the new activity
                newEventIntent.putExtra("modelUser", modelUser);
                getActivity().startActivityForResult(newEventIntent, 2);
            }
        });
        return view;
    }
}
