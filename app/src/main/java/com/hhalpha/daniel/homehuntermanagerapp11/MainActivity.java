package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * Created by Daniel on 5/30/2016.
 */
public class MainActivity extends Activity {
    Button listBtn, createBtn;
    ImageView imageViewTest;
    TextView textViewTest;
    private CognitoCachingCredentialsProvider credentialsProvider;
    CognitoSyncManager syncClient;
    AmazonS3 s3;
    TransferUtility transferUtility;
    DynamoDBMapper mapper;
    AmazonDynamoDB dynamoDB;
    ArrayList<String> addresses;
    File pic1;
    Bitmap bitmapTest;
    FileOutputStream fos;
    String keyTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Log.v("_danoncreate",getApplicationContext().getCacheDir().listFiles().toString());


            addresses=new ArrayList<String>();

            listBtn = (Button) findViewById(R.id.listBtn);
            createBtn = (Button) findViewById(R.id.createBtn);
            imageViewTest = (ImageView) findViewById(R.id.imageViewTest);
            textViewTest = (TextView) findViewById(R.id.textViewTest);

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


            new retrieveTask().execute();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void listProperties(View v){

    }

    public void createNewProperty(View v){
        Intent i= new Intent(MainActivity.this, CreateActivity.class);
        startActivity(i);
    }

    public class retrieveTask extends AsyncTask<String,Integer,ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {
                s3 = new AmazonS3Client(credentialsProvider);

                // Set the region of your S3 bucket
                s3.setRegion(Region.getRegion(Regions.US_EAST_1));
                transferUtility = new TransferUtility(s3, getApplicationContext());
                for (S3ObjectSummary summary : S3Objects.inBucket(s3, "hhproperties")) {
                    addresses.add(summary.getKey());
                    Log.v("_dan", summary.getKey());
                    keyTest=summary.getKey();
                }
                S3ObjectInputStream content = s3.getObject("hhproperties", keyTest).getObjectContent();
                byte[] bytes = IOUtils.toByteArray(content);
                bitmapTest = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Log.v("_dan", addresses.toString());

            } catch (Exception e) {
                Log.v("_dandoib", e.getMessage());
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            textViewTest.setText(strings.toString());
            imageViewTest.setImageBitmap(bitmapTest);
        }
    }
}
