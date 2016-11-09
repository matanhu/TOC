package com.example.matanhuja.finalproject.TimePicker;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import java.util.Calendar;

public class TimeEditText extends EditText implements TimePickerFragment.onTimeSetListener
{
    String hour;
    String minute;

    public TimeEditText(Context context)
    {
        super(context);
        init();
    }

    public TimeEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public TimeEditText(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        setInputType(0);
        Calendar cal = Calendar.getInstance();
        hour = Integer.toString(cal.get(Calendar.HOUR));
        minute = Integer.toString(cal.get(Calendar.MINUTE));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            TimePickerFragment tpf = new TimePickerFragment();
            tpf.setOnTimeSetListener(this);
            tpf.setTime(Integer.parseInt(hour), Integer.parseInt(minute));
            tpf.show(((Activity) getContext()).getFragmentManager(), "TAG");
        }
        return true;
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute)
    {
        if(hourOfDay < 10) {
            this.hour = "0" + Integer.toString(hourOfDay);
        }
        else{
            this.hour =Integer.toString(hourOfDay);
        }
        if(minute<10) {
            this.minute = "0" + Integer.toString(minute);
        }
        else {
            this.minute = Integer.toString(minute);
        }
        setText("" + this.hour + ":" + this.minute);
    }
}

