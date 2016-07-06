package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;
import com.imanoweb.calendarview.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Daniel on 7/5/2016.
 */
public class ScheduleActivity extends Activity {
    String string;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        try{
            Bundle bundle = getIntent().getBundleExtra("bundle");
            string=bundle.getString("string");
            Log.v("_danschedule",string);
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            //Initialize CustomCalendarView from layout
            CustomCalendarView calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

//Initialize calendar with date
            Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());



//Show/hide overflow days of a month
            calendarView.setShowOverflowDate(false);

//call refreshCalendar to update calendar the view
            calendarView.refreshCalendar(currentCalendar);

//Handling custom calendar events
            calendarView.setCalendarListener(new CalendarListener() {
                @Override
                public void onDateSelected(Date date) {
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    Toast.makeText(ScheduleActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
                    CustomDialogClass cdd=new CustomDialogClass(ScheduleActivity.this);
                    cdd.show();
                }

                @Override
                public void onMonthChanged(Date date) {
                    SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                    Toast.makeText(ScheduleActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
