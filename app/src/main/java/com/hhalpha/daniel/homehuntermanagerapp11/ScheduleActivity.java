package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
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
    CustomCalendarView calendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

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
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                Toast.makeText(ScheduleActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });
        //Setting custom font
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Arch_Rival_Bold.ttf");
        if (null != typeface) {
            calendarView.setCustomTypeface(typeface);
            calendarView.refreshCalendar(currentCalendar);
        }


    }
}
