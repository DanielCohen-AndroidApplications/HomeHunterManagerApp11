package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
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
    TextView textDia;
    CustomCalendarView calendarView;
    Calendar currentCalendar;
    List<DayDecorator> list;
    ArrayList<Date> dates;
    Date parsedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        dates=new ArrayList<>();
        try {
            dates.add(new SimpleDateFormat("dd-MM-yyyy").parse("10-07-2016"));
            Log.v("_dan dates",new SimpleDateFormat("dd-MM-yyyy").parse("10-07-2016").toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Bundle bundle = getIntent().getBundleExtra("bundle");
            string=bundle.getString("string");
            Log.v("_danschedule",string);
        }catch(Exception e){
            e.printStackTrace();
        }
        try {
            list=new ArrayList<>();
            list.add(new DaysDecorator());

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            //Initialize CustomCalendarView from layout
            calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

//Initialize calendar with date
            currentCalendar = Calendar.getInstance(Locale.getDefault());

            calendarView.setDecorators(list);

//Show/hide overflow days of a month
            calendarView.setShowOverflowDate(false);

//call refreshCalendar to update calendar the view
            calendarView.refreshCalendar(currentCalendar);

//Handling custom calendar events
            calendarView.setCalendarListener(new CalendarListener() {
                @Override
                public void onDateSelected(Date date) {
                    Bundle bundle = new Bundle();
                    bundle.putString("address",string.replace("[","").replace("+",""));
                    bundle.putString("date",date.toString());
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    Toast.makeText(ScheduleActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
                    CustomDialogClass cdd=new CustomDialogClass(ScheduleActivity.this, bundle);
//                    cdd.setTitle(string.replace("[","").replace("+",""));

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
    private class DaysDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            if(isPastDay(dayView.getDate())){
                dayView.setBackgroundColor(Color.parseColor("#a7a7FF"));
            }
//            try {
//                parsedDate = dayView.getDate();
//                Log.v("_dan parsedDate",parsedDate.toString());
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//            try {
            try {
                Log.v("_dan datedeco", dates.toString().split(" ")[0] + dates.toString().split(" ")[1] + dates.toString().split(" ")[2]);
                Log.v("_dan datedeco2", dayView.getDate().toString().split(" ")[0]+dayView.getDate().toString().split(" ")[1]+dayView.getDate().toString().split(" ")[2]);
            }catch (Exception e){
                e.printStackTrace();
            }
                if (dates.toString().contains(dayView.getDate().toString().split(" ")[0]+" "+dayView.getDate().toString().split(" ")[1]+" "+dayView.getDate().toString().split(" ")[2])) {
                    dayView.setBackgroundColor(Color.parseColor("#FFa7a7"));
                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
        }
    }
    private boolean isPastDay(Date date) {
        Calendar c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        // and get that as a Date
        Date today = c.getTime();

        // test your condition, if Date specified is before today
        if (date.before(today)) {
            return true;
        }
        return false;
    }
}
