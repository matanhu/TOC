package com.example.matanhuja.finalproject.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.matanhuja.finalproject.DetailsEventActivity;
import com.example.matanhuja.finalproject.MainActivity;
import com.example.matanhuja.finalproject.Model.Model;
import com.example.matanhuja.finalproject.Model.ModelEvent;
import com.example.matanhuja.finalproject.Model.ModelUser;
import com.example.matanhuja.finalproject.R;

import java.util.List;

public class ListEventsFragment extends Fragment
{
    ListView listView;
    List<ModelEvent> modelEvents;
    MyAddapter adapter;
    ModelUser modelUser;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_events, container, false);
        listView = (ListView) view.findViewById(R.id.event_listview);

        //Get UID User To List Fragment
        Bundle args = getArguments();
        modelUser = (ModelUser)args.getSerializable("modelUser");
        loadEventsData();
        modelEvents = Model.instance().getEvents();
        adapter = new MyAddapter();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailsIntent = new Intent(getActivity(), DetailsEventActivity.class);
                detailsIntent.putExtra("Event_Id", modelEvents.get(position).getEventId()); //Send the data from intent to the new activity
                detailsIntent.putExtra("modelUser", modelUser);
                startActivityForResult(detailsIntent, 2);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //Check if the resultCode is 2 - list updated and must refresh
        if (resultCode == 2) {
            modelEvents = Model.instance().getEvents();
            adapter.notifyDataSetChanged(); //Refresh list
        }
    }

    class MyAddapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return modelEvents.size();
        }

        @Override
        public Object getItem(int position)
        {
            return modelEvents.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if(convertView == null)
            {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.fragment_list_row, null);
                Log.d("TAG", "create view:" + position);
            }
            else
            {
                Log.d("TAG", "use convert view:" + position);
            }

            final TextView eventName = (TextView) convertView.findViewById(R.id.list_row_event_name);
            final TextView eventTimeStart = (TextView) convertView.findViewById(R.id.list_row_time_start);
            final TextView eventDateStart = (TextView) convertView.findViewById(R.id.list_row_date_start);
            final TextView eventTimeEnd = (TextView) convertView.findViewById(R.id.list_row_time_end);
            final TextView eventDateEnd = (TextView) convertView.findViewById(R.id.list_row_date_end);
            final ModelEvent modelEvent = modelEvents.get(position);

            eventName.setText(modelEvent.getEventName());
            eventTimeStart.setText(modelEvent.getStartTime());
            eventDateStart.setText(modelEvent.getStartDate());
            eventTimeEnd.setText(modelEvent.getEndTime());
            eventDateEnd.setText(modelEvent.getEndDate());
            return convertView;
        }

    }

    public void loadEventsData()
    {
        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.startProgressbar();
        Model.getInstance().getAllEventsFromDB(modelUser.getEmail().trim(), new Model.getEventsListener()
        {
            @Override
            public void onResult(List<ModelEvent> events)
            {
                modelEvents = events;
                adapter.notifyDataSetChanged();
                mainActivity.stopProgressbar();
            }
            @Override
            public void onCancel()
            {

            }
        });
    }
}
