package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBDeleteExpression;
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
import com.facebook.Profile;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 7/6/2016.
 */
public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;

    public Button yes,btn_delete;
    TextView txt_dia;
    AmazonDynamoDBClient ddbClient;
    Spinner spinnerHours, spinnerMinutes;
    String hours, mins, oldHours,oldMins, date, address;
    DynamoDBMapper mapper;
    AmazonDynamoDB dynamoDB;
    String idPool;
    CognitoCachingCredentialsProvider credentialsProvider;
    CognitoSyncManager syncClient;
    Timeslot timeslot;
    CheckBox checkBoxAM, checkBoxPM;
    String amPm;
    Boolean available, requested, confirmed,edit,deleting;
    int clicks;
    Appointment appointment;
    ConfirmedAppointment confAppt;
    Boolean confirming;
    public CustomDialogClass(Activity a, Bundle args) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        address=args.getString("address");
        date=args.getString("date");
        try{
            edit=args.getBoolean("edit");
        }catch (Exception e){
            edit=false;
            e.printStackTrace();
        }
        try {
            available = args.getBoolean("available");
            requested = args.getBoolean("requested");
            confirmed = args.getBoolean("confirmed");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.custom_dialog);
        FacebookSdk.sdkInitialize(getContext());
        try{
            credentialsProvider = new CognitoCachingCredentialsProvider(
                    getContext(),
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
        clicks=0;
        deleting=false;
        try{
            Log.v("_dan date",date);
            Log.v("_dan available",available.toString());
            Log.v("_dan requested",requested.toString());
            Log.v("_dan conf",confirmed.toString());
            Log.v("_dan edit",edit.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

        checkBoxPM=(CheckBox) findViewById(R.id.checkBoxPM);
        checkBoxAM=(CheckBox) findViewById(R.id.checkBoxAM);
        txt_dia=(TextView)findViewById(R.id.txt_dia);
        txt_dia.setText(date.split(" ")[0].toString()+" "+date.split(" ")[1].toString()+" "+date.split(" ")[2].toString()+"\n"+address);
        yes = (Button) findViewById(R.id.btn_yes);
        btn_delete=(Button) findViewById(R.id.btn_delete);
        yes.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
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

            }
        });
        if(edit){
            btn_delete.setVisibility(View.VISIBLE);
            if(requested){yes.setText("Confirm");}
            try {
                oldHours = date.split(" ")[3].split(":")[0];
                oldMins = date.split(" ")[3].split(":")[1];
                if(Integer.parseInt(oldHours)<13) {
                    spinnerHours.setSelection(Integer.parseInt(oldHours), true);
                    checkBoxAM.setChecked(true);
                    amPm=" AM ";
                }else{
                    spinnerHours.setSelection(Integer.parseInt(oldHours)-12, true);
                    Integer oldHoursAdjusted=Integer.parseInt(oldHours)-12;
                    oldHours= oldHoursAdjusted.toString();
                    checkBoxPM.setChecked(true);
                    amPm=" PM ";
                }
                if(Integer.parseInt(oldHours)<10&&oldHours.length()>1){
                    oldHours="0"+oldHours;
                }
                spinnerMinutes.setSelection((Integer.parseInt(oldMins) / 15) + 1, true);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

//        new dynamoTask().execute();
//        new retrieveTask().execute();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_yes) {
            Log.v("_dandia", address + "hours=" + hours + "mins=" + mins);
            confirming = true;
            deleting = false;
            if (checkBoxPM.isChecked()) {
                amPm = " PM ";
            } else {
                amPm = " AM ";
            }
            new dynamoTask().execute();
        }else{
            deleting=true;
            confirming=false;
            new dynamoTask().execute();
        }

    }
    public class dynamoTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.v("_dan deleting", deleting.toString());
            Log.v("_dan confirming", confirming.toString());
            syncClient = new CognitoSyncManager(
                    getContext(),
                    Regions.US_EAST_1, // Region
                    credentialsProvider);
            credentialsProvider.refresh();
            ddbClient = new AmazonDynamoDBClient(credentialsProvider);
            mapper = new DynamoDBMapper(ddbClient);

            try {
                Log.v("_dan time string uno",date.split(" ")[0].toString().replace("[","").replace("]","")+" "+date.split(" ")[1].toString()+" "+date.split(" ")[2].toString()+" "+oldHours+":"+oldMins+amPm+date.split(" ")[5].toString().replace("[","").replace("]","")+" @ "+address);
                if(deleting&&available){
                    timeslot=new Timeslot();
                    try{
                        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                        PaginatedScanList<Timeslot> result = mapper.scan(Timeslot.class, scanExpression);
                        for(int i=0;i<result.size();i++) {
                            if (result.get(i).getTime().split("@")[1].contains(address.replace("[", "").replace("]", "").replace("+", "").replace(",", ""))) {
                                Log.v("_dan custdialog result",result.get(i).getTime().split("@")[1]);
//                                confAppt.setHost(result.get(i).getHost());
//                                confAppt.setTime(result.get(i).getTime());
                                timeslot=result.get(i);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        mapper.delete(timeslot);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(deleting && requested) {
                    appointment=new Appointment();
                    try{
                        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                        PaginatedScanList<Appointment> result = mapper.scan(Appointment.class, scanExpression);
                        for(int i=0;i<result.size();i++) {
                            if (result.get(i).getTime().split("@")[1].contains(address.replace("[", "").replace("]", "").replace("+", "").replace(",", ""))) {
                                Log.v("_dan custdialog result",result.get(i).getTime().split("@")[1]);
//                                confAppt.setHost(result.get(i).getHost());
//                                confAppt.setTime(result.get(i).getTime());
                                appointment=result.get(i);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        mapper.delete(appointment);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(confirming&&requested) {
                    appointment=new Appointment();
                    confAppt= new ConfirmedAppointment();
                    try{
                        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                        PaginatedScanList<Appointment> result = mapper.scan(Appointment.class, scanExpression);
                        for(int i=0;i<result.size();i++) {
                            if (result.get(i).getTime().split("@")[1].contains(address.replace("[", "").replace("]", "").replace("+", "").replace(",", "").substring(1))) {
                                Log.v("_dan custdialog result",result.get(i).getTime().split("@")[1]);
                                confAppt.setHost(result.get(i).getHost());
                                confAppt.setTime(result.get(i).getTime());
                                appointment=result.get(i);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        mapper.save(confAppt);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        mapper.delete(appointment);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(deleting && confirmed) {
                    confAppt= new ConfirmedAppointment();
                    try{
                        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                        PaginatedScanList<ConfirmedAppointment> result = mapper.scan(ConfirmedAppointment.class, scanExpression);
                        for(int i=0;i<result.size();i++) {
                            if (result.get(i).getTime().split("@")[1].contains(address.replace("[", "").replace("]", "").replace("+", "").replace(",", ""))) {
                                Log.v("_dan custdialog result",result.get(i).getTime().split("@")[1]);
//                                confAppt.setHost(result.get(i).getHost());
//                                confAppt.setTime(result.get(i).getTime());
                                confAppt=result.get(i);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        mapper.delete(confAppt);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(!edit){
                    timeslot=new Timeslot();
                    timeslot.setTime(date.split(" ")[0].toString() + " " + date.split(" ")[1].toString() + " " + date.split(" ")[2].toString() + " " + hours + ":" + mins + amPm + date.split(" ")[5].toString() + " @ " + address);
                    timeslot.setHost(Profile.getCurrentProfile().getName());
                    mapper.save(timeslot);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            deleting=false;
            confirming=false;
            dismiss();
        }

    }



}