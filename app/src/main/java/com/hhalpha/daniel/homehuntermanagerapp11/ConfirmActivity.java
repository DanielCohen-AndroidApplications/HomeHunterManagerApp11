package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
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
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 5/30/2016.
 */

public class ConfirmActivity extends Activity implements OnMapReadyCallback {
    String address, shortBlurb, bigBlurb, payPeriod, minSalary,beds, baths, sqft, rent;

    Boolean guarantor, dogs, smallDogs, cats, couples, children, smoking, securityDeposit, doorman;
    TextView textViewAddress,textViewSqft,textViewRent, textViewMinSalary, textViewShort, textViewLong, textViewBeds, textViewBaths, textViewCouples, textViewChildren, textViewSmallDogs, textViewDogs, textViewCats, textViewSmoking, textViewGuarantor, textViewSecurity, textViewDoorman;
    Bundle bundle;
    ArrayList<String> arrayList;
    private CognitoCachingCredentialsProvider credentialsProvider;
    CognitoSyncManager syncClient;
    AmazonS3 s3;
    TransferUtility transferUtility;
    DynamoDBMapper mapper;
    AmazonDynamoDB dynamoDB;
    Property property;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
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

            @Override
            public DescribeLimitsResult describeLimits() throws AmazonServiceException, AmazonClientException {
                return null;
            }

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
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bundle=getIntent().getBundleExtra("bundle");
        textViewAddress=(TextView) findViewById(R.id.textViewAddress);
        textViewRent=(TextView) findViewById(R.id.textViewRent);
        textViewSqft=(TextView) findViewById(R.id.textViewSqft);
        textViewMinSalary=(TextView) findViewById(R.id.textViewMinSalary);
        textViewShort=(TextView) findViewById(R.id.textViewShort);
        textViewLong=(TextView) findViewById(R.id.textViewLong);
        textViewBeds=(TextView) findViewById(R.id.textViewBeds);
        textViewBaths=(TextView) findViewById(R.id.textViewBaths);
        textViewCouples=(TextView) findViewById(R.id.textViewCouples);
        textViewChildren=(TextView) findViewById(R.id.textViewChildren);
        textViewSmallDogs=(TextView) findViewById(R.id.textViewSmallDogs);
        textViewDogs=(TextView) findViewById(R.id.textViewDogs);
        textViewCats=(TextView) findViewById(R.id.textViewCats);
        textViewSmoking=(TextView) findViewById(R.id.textViewSmoking);
        textViewGuarantor=(TextView) findViewById(R.id.textViewGuarantor);
        textViewSecurity=(TextView) findViewById(R.id.textViewSecurity);
        textViewDoorman=(TextView) findViewById(R.id.textViewDoorman);

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "us-east-1:ceae0626-1082-4759-85c3-fae01752889a", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.US_EAST_1, // Region
                credentialsProvider);
        CognitoSyncManager syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.US_EAST_1, // Region
                credentialsProvider);
        AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        mapper = new DynamoDBMapper(ddbClient);
        property = new Property();

        s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
        transferUtility = new TransferUtility(s3, getApplicationContext());



        try {
//            Log.v("_dan", bundle.getStringArrayList("arrayList").get(0));
//            Log.v("_dan", bundle.getStringArrayList("arrayList").get(1));
            arrayList = bundle.getStringArrayList("arrayList");
            for(int i=0;i<arrayList.size();i++){
                Log.v("_dan",arrayList.get(i));
            }
            textViewAddress.setText("Address: "+arrayList.get(0));
            address=arrayList.get(0);
            textViewSqft.setText(arrayList.get(1)+" Sqft");
            textViewRent.setText("Rent:"+arrayList.get(2));
            textViewMinSalary.setText("Minimum Salary:"+arrayList.get(3));
            textViewShort.setText(arrayList.get(4));
            textViewRent.setText(arrayList.get(5));
            textViewBeds.setText(arrayList.get(6)+ " Beds");
            textViewBaths.setText(arrayList.get(7)+" Baths");
            if(arrayList.get(8).equals("true")) {
                textViewCouples.setText("Couples OK");
            } else if(arrayList.get(8).equals("false")) {
                textViewCouples.setText("No Couples");
            }
            if(arrayList.get(9).equals("true")) {
                textViewChildren.setText("Children OK");
            } else if(arrayList.get(9).equals("false")) {
                textViewChildren.setText("No Children");
            }
            if(arrayList.get(10).equals("true")) {
                textViewSmallDogs.setText("Small Dogs OK");
            } else if(arrayList.get(10).equals("false")) {
                textViewCouples.setText("No Small Dogs");
            }
            if(arrayList.get(11).equals("true")) {
                textViewDogs.setText("Large Dogs OK");
            } else if(arrayList.get(11).equals("false")) {
                textViewDogs.setText("No Large Dogs");
            }
            if(arrayList.get(12).equals("true")) {
                textViewCats.setText("Cats OK");
            } else if(arrayList.get(12).equals("false")) {
                textViewCats.setText("No Cats");
            }
            if(arrayList.get(13).equals("true")) {
                textViewSmoking.setText("Smoking OK");
            } else if(arrayList.get(13).equals("false")) {
                textViewSmoking.setText("No Smoking");
            }
            if(arrayList.get(14).equals("true")) {
                textViewGuarantor.setText("Guarantor OK");
            } else if(arrayList.get(14).equals("false")) {
                textViewGuarantor.setText("No Guarantors");
            }
            if(arrayList.get(15).equals("true")) {
                textViewSecurity.setText("Security Deposit Required");
            } else if(arrayList.get(15).equals("false")) {
                textViewSecurity.setText("No Security Deposit");
            }
            if(arrayList.get(15).equals("true")) {
                textViewDoorman.setText("Doorman");
            } else if(arrayList.get(15).equals("false")) {
                textViewDoorman.setText("No Doorman");
            }
        }catch (Exception e ){
            Log.v("_dan", e.getMessage());
        }

    }
    @Override
    public void onMapReady(GoogleMap map) {

        LatLng sydney = new LatLng(-33.867, 151.206);
        try {
            map.setMyLocationEnabled(true);
        }catch (SecurityException e){
            Log.v("_dan", e.getMessage());
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));
    }
    public void confirm(View v){
        try {
            new dataTask().execute();
        }catch (Exception e){
            Log.v("_dan",e.getMessage());
        }
    }
    public class dataTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                //DYNAMO: insert attribute into "Music" Table.
                property.setAddress(address);
                property.setDataString(arrayList.toString());
                mapper.save(property);
//                mapper.notify();
//                C:\Users\Daniel\Desktop\DanTest1-aws-my-sample-app-android\DanAwsTest\app\src\main\res\raw\pic1.PNG
//                //S3: upload pic to "hhproperties" S3 Bucket
//
//                Uri pic1_path = Uri.parse("android.resource://"+getPackageName()+"/raw/pic1");
//                File pic1 = new File(pic1_path.toString());
//                TransferObserver observer = transferUtility.upload(
//                        "hhproperties",     /* The bucket to upload to */
//                        "pic1",    /* The key for the uploaded object */
//                        pic1);     /* The file where the data to upload exists */
//                observer.refresh();
//                s3.putObject("hhproperties",     /* The bucket to upload to */
//                        "pic1",    /* The key for the uploaded object */
//                        pic1);
//                putItemRequest=new PutItemRequest();
//                putItemRequest.setTableName("Music");
//
//                attributeValue=new AttributeValue();
//                attributeValue.setS("GD");
//                mapRequest.put("Artist", attributeValue);
//                putItemRequest.setItem(mapRequest);
            }catch (Exception e){

            }


            return "Executed";
        }
    }
}
