package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedScanList;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemResult;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeLimitsRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeLimitsResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.amazonaws.services.dynamodbv2.model.UpdateTableRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateTableResult;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;
import com.imanoweb.calendarview.*;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Daniel on 7/5/2016.
 */
public class ScheduleActivity extends Activity {
    String string;
    CustomCalendarView calendarView;
    Calendar currentCalendar;
    List<DayDecorator> list;
    ArrayList<Date> dates, appts, confAppts;
    ArrayList<ArrayList<Date>> dateArrayLists;
    DynamoDBMapper mapper;
    CognitoCachingCredentialsProvider credentialsProvider;
    CognitoSyncManager syncClient;
    Timeslot timeslot;
    AmazonDynamoDB dynamoDB;
    AmazonDynamoDBClient ddbClient;
    ArrayList<Integer> apptIndicies;
    int apptIndex, numSlots, numAppts, numConfAppts;
    String status;
    ArrayList<String> dateArrayList, apptArrayList, confApptArrayList, statusList;
    Boolean available, requested, confirmed;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        bundle=new Bundle();
        available=false;
        requested=false;
        confirmed=false;
        dates=new ArrayList<>();
        appts=new ArrayList<>();
        confAppts=new ArrayList<>();
        apptIndicies=new ArrayList<>();
        dateArrayList = new ArrayList<>();
        dateArrayLists=new ArrayList<>();
        apptArrayList=new ArrayList<>();
        confApptArrayList=new ArrayList<>();

        //Initialize CustomCalendarView from layout
        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        FacebookSdk.sdkInitialize(getApplicationContext());
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
        dynamoDB=new AmazonDynamoDB() {
            @Override
            public void setEndpoint(String endpoint) throws IllegalArgumentException {

            }

            @Override
            public void setRegion(Region region) throws IllegalArgumentException {

            }

            @Override
            public CreateTableResult createTable(CreateTableRequest createTableRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public ScanResult scan(ScanRequest scanRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public DescribeLimitsResult describeLimits(DescribeLimitsRequest describeLimitsRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public BatchGetItemResult batchGetItem(BatchGetItemRequest batchGetItemRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public GetItemResult getItem(GetItemRequest getItemRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public ListTablesResult listTables(ListTablesRequest listTablesRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public BatchWriteItemResult batchWriteItem(BatchWriteItemRequest batchWriteItemRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public DeleteTableResult deleteTable(DeleteTableRequest deleteTableRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public DeleteItemResult deleteItem(DeleteItemRequest deleteItemRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public UpdateTableResult updateTable(UpdateTableRequest updateTableRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public UpdateItemResult updateItem(UpdateItemRequest updateItemRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public DescribeTableResult describeTable(DescribeTableRequest describeTableRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public QueryResult query(QueryRequest queryRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public PutItemResult putItem(PutItemRequest putItemRequest) throws AmazonServiceException, AmazonClientException {
                return null;
            }

//            @Override
//            public DescribeLimitsResult describeLimits() throws AmazonServiceException, AmazonClientException {
//                return null;
//            }

            @Override
            public ListTablesResult listTables() throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public CreateTableResult createTable(List<AttributeDefinition> attributeDefinitions, String tableName, List<KeySchemaElement> keySchema, ProvisionedThroughput provisionedThroughput) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public ScanResult scan(String tableName, List<String> attributesToGet) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public ScanResult scan(String tableName, Map<String, Condition> scanFilter) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public ScanResult scan(String tableName, List<String> attributesToGet, Map<String, Condition> scanFilter) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public BatchGetItemResult batchGetItem(Map<String, KeysAndAttributes> requestItems, String returnConsumedCapacity) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public BatchGetItemResult batchGetItem(Map<String, KeysAndAttributes> requestItems) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public GetItemResult getItem(String tableName, Map<String, AttributeValue> key) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public GetItemResult getItem(String tableName, Map<String, AttributeValue> key, Boolean consistentRead) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public ListTablesResult listTables(String exclusiveStartTableName) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public ListTablesResult listTables(String exclusiveStartTableName, Integer limit) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public ListTablesResult listTables(Integer limit) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public BatchWriteItemResult batchWriteItem(Map<String, List<WriteRequest>> requestItems) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public DeleteTableResult deleteTable(String tableName) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public DeleteItemResult deleteItem(String tableName, Map<String, AttributeValue> key) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public DeleteItemResult deleteItem(String tableName, Map<String, AttributeValue> key, String returnValues) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public UpdateTableResult updateTable(String tableName, ProvisionedThroughput provisionedThroughput) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public UpdateItemResult updateItem(String tableName, Map<String, AttributeValue> key, Map<String, AttributeValueUpdate> attributeUpdates) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public UpdateItemResult updateItem(String tableName, Map<String, AttributeValue> key, Map<String, AttributeValueUpdate> attributeUpdates, String returnValues) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public DescribeTableResult describeTable(String tableName) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public PutItemResult putItem(String tableName, Map<String, AttributeValue> item) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public PutItemResult putItem(String tableName, Map<String, AttributeValue> item, String returnValues) throws AmazonServiceException, AmazonClientException {
                return null;
            }

            @Override
            public void shutdown() {

            }

            @Override
            public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest request) {
                return null;
            }
        };
        new dynamoTask().execute();

        try{
            Bundle bundle = getIntent().getBundleExtra("bundle");
            string=bundle.getString("string");
            Log.v("_danschedule",string);
        }catch(Exception e){
            e.printStackTrace();
        }
        initializeCalendar();

        new retrieveTask().execute();
        new retrieveApptsTask().execute();
        new retrieveConfApptsTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeCalendar();
    }

    public class DaysDecorator implements DayDecorator {

        @Override
        public void decorate(final DayView dayView) {
            numSlots=0;
            numAppts=0;
            numConfAppts=0;
            if (isPastDay(dayView.getDate())) {
                dayView.setBackgroundColor(Color.parseColor("#a7a7FF"));
            }else {//determine if each date includes available, requested, or confirmed time slots, and if so, how many.
                for (int i = 0; i < dates.size(); i++) {
                    apptIndex = i;
                    if (dates.get(i).toString().replace("[", "").replace("]", "").contains(dayView.getDate().toString().split(" ")[0] + " " + dayView.getDate().toString().split(" ")[1] + " " + dayView.getDate().toString().split(" ")[2])) {
//                        statusList.add("available");
//                        available=true;
                        numSlots++;
                        dayView.setBackgroundColor(Color.parseColor("#cca7a7"));
                    }


                }
                for (int y = 0; y < appts.size(); y++) {
                    apptIndex = y;
                    if (appts.get(y).toString().replace("[", "").replace("]", "").contains(dayView.getDate().toString().split(" ")[0] + " " + dayView.getDate().toString().split(" ")[1] + " " + dayView.getDate().toString().split(" ")[2])) {
//                        statusList.add("requested");
//                        requested=true;
                        numAppts++;
                        dayView.setBackgroundColor(Color.parseColor("#a7a7bb"));
                    }
                }

                for (int z = 0; z < confAppts.size(); z++) {
//                    statusList.add("confirmed");
                    apptIndex = z;
                    Log.v("_dan confAppts" + z, confAppts.get(z).toString());
                    Log.v("dan dayview get date", dayView.getDate().toString());
                    if (confAppts.get(z).toString().replace("[", "").replace("]", "").contains(dayView.getDate().toString().split(" ")[0] + " " + dayView.getDate().toString().split(" ")[1] + " " + dayView.getDate().toString().split(" ")[2])) {
//                        confirmed=true;
                        numConfAppts++;
                        dayView.setBackgroundColor(Color.parseColor("#00FF00"));
                    }

                }

                if(numSlots>0||numAppts>0||numConfAppts>0) {
                    dayView.setText(dayView.getDate().toString().split(" ")[2]+"\n"+numConfAppts + " appointments confirmed!"+ "\n"+numSlots + " timeslots set as available" + "\n" + numAppts + " timeslots awaiting confirmation");
                    dayView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try{
                                for(int x=0;x<dates.toString().split(",").length;x++){
                                    Log.v("_dan sched dates",dates.toString().split(",")[x]);
                                    Log.v("_dan sched dayview",dayView.getDate().toString());
                                    if(dates.toString().split(",")[x].contains(dayView.getDate().toString().split(" ")[0]+" "+dayView.getDate().toString().split(" ")[1]+" "+dayView.getDate().toString().split(" ")[2])){
                                        dateArrayList.add(dates.toString().split(",")[x]);
                                    }
                                }
//                                bundle.putBoolean("available",available);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            try{
                                for(int x=0;x<appts.toString().split(",").length;x++){
                                    Log.v("_dan sched appts",appts.toString().split(",")[x]);
                                    if(appts.toString().split(",")[x].contains(dayView.getDate().toString().split(" ")[0]+" "+dayView.getDate().toString().split(" ")[1]+" "+dayView.getDate().toString().split(" ")[2])){
                                        apptArrayList.add(appts.toString().split(",")[x]);
//                                        requested=true;
                                    }
                                }
//                                bundle.putBoolean("requested",requested);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            try{
                                for(int x=0;x<confAppts.toString().split(",").length;x++){
                                    Log.v("_dan sched conf appts",confAppts.toString().split(",")[x]);
                                    if(confAppts.toString().split(",")[x].contains(dayView.getDate().toString().split(" ")[0]+" "+dayView.getDate().toString().split(" ")[1]+" "+dayView.getDate().toString().split(" ")[2])){
                                        confApptArrayList.add(confAppts.toString().split(",")[x]);
//                                        confirmed=true;
                                    }
                                }
//                                bundle.putBoolean("confirmed",confirmed);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            try {
                                bundle = new Bundle();
                                bundle.putString("date", dayView.getDate().toString());//
                                if(!string.isEmpty()){bundle.putString("address", string.replace("[", "").replace("]","").replace("+", ""));}
//                                if(!statusList.isEmpty()){bundle.putStringArrayList("statusList",statusList);}
                                if(!dateArrayList.isEmpty()){bundle.putStringArrayList("dateArrayList",dateArrayList);
                                    available= true;}
                                bundle.putBoolean("available",available);
                                Log.v("_dan sched dates2",dateArrayList.toString());
                                Log.v("_dan sched avail",available.toString());
                                if(!apptArrayList.isEmpty()){bundle.putStringArrayList("apptArrayList",apptArrayList);
                                    requested= true;}
                                bundle.putBoolean("requested",requested);
                                Log.v("_dan sched appts2",apptArrayList.toString());
                                Log.v("_dan sched avail",requested.toString());
                                if(!confApptArrayList.isEmpty()){bundle.putStringArrayList("confApptArrayList",confApptArrayList);
                                    confirmed= true;}
                                bundle.putBoolean("confirmed",confirmed);
                                Log.v("_dan sched confAppt2",confApptArrayList.toString());
                                Log.v("_dan sched conf",confirmed.toString());
                                CustomListDialog cdd = new CustomListDialog(ScheduleActivity.this, bundle);
                                cdd.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

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
    public class dynamoTask extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... params) {
            syncClient = new CognitoSyncManager(
                    getApplicationContext(),
                    Regions.US_EAST_1, // Region
                    credentialsProvider);
            credentialsProvider.refresh();
            AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            mapper = new DynamoDBMapper(ddbClient);
            timeslot=new Timeslot();

            return null;
        }
    }
    public class retrieveTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            syncClient = new CognitoSyncManager(
                    getApplicationContext(),
                    Regions.US_EAST_1, // Region
                    credentialsProvider);
            credentialsProvider.refresh();
            ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            mapper = new DynamoDBMapper(ddbClient);


            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            PaginatedScanList<Timeslot> result = mapper.scan(Timeslot.class, scanExpression);
            for(int i=0;i<result.size();i++) {
                try{
                    if(result.get(i).getTime().toString().split("@")[1].toString().contains(string.replace("[","").replace("]","").replace("+","").replace(",",""))) {
                        dates.add(new SimpleDateFormat("EEE MMM dd hh:mm a yyyy").parse(result.get(i).getTime().toString().split("@")[0].toString()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.v("_dan ddbScan", result.get(i).getTime().toString());

            }
            Log.v("_dan dates after scan", dates.toString());

            return null;
        }



    }
    public class retrieveApptsTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            syncClient = new CognitoSyncManager(
                    getApplicationContext(),
                    Regions.US_EAST_1, // Region
                    credentialsProvider);
            credentialsProvider.refresh();
            ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            mapper = new DynamoDBMapper(ddbClient);


            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            PaginatedScanList<Appointment> result = mapper.scan(Appointment.class, scanExpression);
            for(int i=0;i<result.size();i++) {
                try{
                    if(result.get(i).getTime().split("@")[1].contains(string.replace("[","").replace("]","").replace("+","").replace(",",""))) {
                        appts.add(new SimpleDateFormat("EEE MMM dd hh:mm a yyyy", Locale.US).parse(result.get(i).getTime().split("@")[0]));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.v("_dan ddbScan", result.get(i).getTime().toString());

            }
            Log.v("_dan dates after scan", dates.toString());
//            initializeCalendar();
            return null;
        }

//        @Override
//        protected void onPostExecute(String s) {
//            initializeCalendar();
//        }


    }

    public class retrieveConfApptsTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            syncClient = new CognitoSyncManager(
                    getApplicationContext(),
                    Regions.US_EAST_1, // Region
                    credentialsProvider);
            credentialsProvider.refresh();
            ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            mapper = new DynamoDBMapper(ddbClient);


            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            PaginatedScanList<ConfirmedAppointment> result = mapper.scan(ConfirmedAppointment.class, scanExpression);
            for(int i=0;i<result.size();i++) {
                try{
                    if(result.get(i).getTime().split("@")[1].contains(string.replace("[","").replace("]","").replace("+","").replace(",",""))) {
                        confAppts.add(new SimpleDateFormat("EEE MMM dd hh:mm a yyyy", Locale.US).parse(result.get(i).getTime().split("@")[0]));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.v("_dan ddbScan", result.get(i).getTime().toString());

            }
            Log.v("_dan dates after scan", dates.toString());
//            initializeCalendar();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            initializeCalendar();
        }


    }
    public void initializeCalendar(){
        try {
            list=new ArrayList<>();
            list.add(new DaysDecorator());

        }catch (Exception e){
            e.printStackTrace();
        }
        try{


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
                    try {
                        Bundle bundle = new Bundle();
                        bundle.putString("address", string.replace("[", "").replace("+", ""));
                        bundle.putString("date", date.toString());
                        SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd hh:mm a yyyy");
                        Toast.makeText(ScheduleActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
                        CustomDialogClass cdd = new CustomDialogClass(ScheduleActivity.this, bundle);
//                    cdd.setTitle(string.replace("[","").replace("+",""));
                        cdd.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                Intent i = getIntent();
                                startActivity(i);
                            }
                        });
                        cdd.show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
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
