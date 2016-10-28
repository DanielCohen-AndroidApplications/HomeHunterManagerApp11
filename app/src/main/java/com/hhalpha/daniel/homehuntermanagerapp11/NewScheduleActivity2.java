package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.facebook.AccessToken;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.security.AccessController.getContext;

/**
 * Created by Daniel on 10/12/2016.
 */

public class NewScheduleActivity2 extends Activity {
    Button yes,btn_delete;
    TextView txt_dia;
    Spinner spinnerHours, spinnerMinutes, spinnerMonths, spinnerDays, spinnerYears;
    String hours, mins, oldHours,oldMins, date, address, day,month,year;
    DynamoDBMapper mapper;
    AmazonDynamoDB dynamoDB;
    AmazonDynamoDBClient ddbClient;
    String idPool;
    CognitoSyncManager syncClient;
    Timeslot timeslot;
    CheckBox checkBoxAM, checkBoxPM;
    String amPm,profile;
    Boolean available, requested, confirmed,edit,deleting;
    int clicks;
    Appointment appointment;
    ConfirmedAppointment confAppt;
    Boolean confirming;
    Bundle bundle;
    SharedPreferences preferences;
    Showing showing;
    ArrayList<Showing> showings;
    ArrayList<String> timeSlots;
    File pic1;
    private CognitoCachingCredentialsProvider credentialsProvider;
    ArrayAdapter<String> timeSlotAdapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newschedule2);
        address=getIntent().getExtras().getString("address");
        showing=new Showing();
        showings=new ArrayList<Showing>();
        timeSlots=new ArrayList<String>();
        timeSlotAdapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, timeSlots);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(timeSlotAdapter);

        try{
            credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "us-east-1:db3a6e00-7c35-4f48-b956-eaf3375a024f", // Identity Pool ID
                    Regions.US_EAST_1 // Region
            );
            Map<String, String> logins = new HashMap<String, String>();
            logins.put("graph.facebook.com", AccessToken.getCurrentAccessToken().getToken());
            credentialsProvider.setLogins(logins);
        }catch (Exception e){
            e.printStackTrace();
        }
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        profile= preferences.getString("profileName","");
        new retrieveTimeSlots().execute();
        checkBoxPM=(CheckBox) findViewById(R.id.checkBoxPM);
        checkBoxAM=(CheckBox) findViewById(R.id.checkBoxAM);
        txt_dia=(TextView)findViewById(R.id.txt_dia);
        //txt_dia.setText(date.split(" ")[0].toString()+" "+date.split(" ")[1].toString()+" "+date.split(" ")[2].toString()+"\n"+address);
        yes = (Button) findViewById(R.id.btn_yes);
        btn_delete=(Button) findViewById(R.id.btn_delete);

        spinnerHours = (Spinner) findViewById(R.id.spinnerHours);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
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

            }
        });
        spinnerMinutes = (Spinner) findViewById(R.id.spinnerMinutes);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(),
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

            }
        });
        spinnerDays = (Spinner) findViewById(R.id.spinnerDays);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.Days, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerDays.setAdapter(adapter3);
        spinnerDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerMonths = (Spinner) findViewById(R.id.spinnerMonths);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.Months, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerMonths.setAdapter(adapter4);
        spinnerMonths.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerYears = (Spinner) findViewById(R.id.spinnerYears);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.Years, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerYears.setAdapter(adapter5);
        spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        checkBoxAM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    amPm="AM";
                    checkBoxPM.setChecked(false);
                }
                if (!isChecked) {
                    amPm="PM";
                }
            }
        });
        checkBoxPM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    amPm="PM";
                    checkBoxAM.setChecked(false);
                }
                if (!isChecked) {
                    amPm="AM";
                }
            }
        });

    }

    public void createAvailableTimeSlot(View v){
        txt_dia.setText(address+hours+":"+mins+" "+amPm+" "+day+" "+month+" "+year+" "+profile);
        //create a map to store user metadata
        showing.setAddress(address);
        showing.setInfoString("Available "+hours+":"+mins+" "+amPm+" "+day+" "+month+" "+year+" "+profile);
        new timeSlotTask().execute(showing);
    }

    public class timeSlotTask extends AsyncTask<Showing,Integer,String>{

        @Override
        protected String doInBackground(Showing... params) {
            syncClient = new CognitoSyncManager(
                    getApplicationContext(),
                    Regions.US_EAST_1, // Region
                    credentialsProvider);
            credentialsProvider.refresh();
            ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            mapper = new DynamoDBMapper(ddbClient);
            mapper.save(params[0]);
            return "";
        }
    }

    public class retrieveTimeSlots extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... params) {
            syncClient = new CognitoSyncManager(
                    getApplicationContext(),
                    Regions.US_EAST_1, // Region
                    credentialsProvider);
            credentialsProvider.refresh();
            ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            mapper = new DynamoDBMapper(ddbClient);
            try{
                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                PaginatedScanList<Showing> result = mapper.scan(Showing.class, scanExpression);
                for(int i=0;i<result.size();i++) {
                    if (result.get(i).getInfoString().contains(profile)&&result.get(i).getAddress().contains(address)){
                        showings.add(result.get(i));
                        timeSlots.add(result.get(i).getInfoString());
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            timeSlotAdapter.notifyDataSetChanged();
        }
    }
}
