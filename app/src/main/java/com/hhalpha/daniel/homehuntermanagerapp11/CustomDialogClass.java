package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.content.ContentResolverCompat;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Daniel on 7/6/2016.
 */
public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;

    public Button yes;
    TextView txt_dia;

    Spinner spinnerHours, spinnerMinutes;
    String hours, mins, date, address;
    public CustomDialogClass(Activity a, Bundle args) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        address=args.getString("address");
        date=args.getString("date");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.custom_dialog);

        txt_dia=(TextView)findViewById(R.id.txt_dia);
        txt_dia.setText(date+"\n"+address);
        yes = (Button) findViewById(R.id.btn_yes);

        yes.setOnClickListener(this);
        spinnerHours = (Spinner) findViewById(R.id.spinnerHours);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Hours, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerHours.setAdapter(adapter);
        spinnerHours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hours=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                hours="00";
            }
        });
        spinnerMinutes = (Spinner) findViewById(R.id.spinnerMinutes);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
                R.array.Minutes, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerMinutes.setAdapter(adapter2);
        spinnerMinutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mins=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mins="00";
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                Log.v("_dandia",address+"hours="+hours+"mins="+mins);

                dismiss();
        }

    }
}
