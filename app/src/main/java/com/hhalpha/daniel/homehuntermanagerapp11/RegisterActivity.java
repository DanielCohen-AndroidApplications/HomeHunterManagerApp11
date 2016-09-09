package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 7/15/2016.
 */
public class RegisterActivity extends FragmentActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView textViewFB;
    AccessToken accessToken;
    Profile profile;
    SharedPreferences preferences;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
//    private CognitoCachingCredentialsProvider credentialsProvider;
    CognitoSyncManager syncClient;
    AmazonS3 s3;
    TransferUtility transferUtility;
    DynamoDBMapper mapper;
    AmazonDynamoDB dynamoDB;
    CognitoCachingCredentialsProvider credentialsProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
//        printHash();
        setContentView(R.layout.activity_register);
        textViewFB=(TextView) findViewById(R.id.textViewFB);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                textViewFB.setText("Success!");
                Log.v("_dan","success");
                try {
                    profile = Profile.getCurrentProfile();
//                    Log.v("_dan",profile.getName());
                }catch (Exception e){
                    e.printStackTrace();
                }
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
                try {
                    preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("registered", true);
                    editor.putString("loginMethod", "FB");
                    editor.putString("profileName", profile.getName());
                    editor.putString("phoneNum", ContactsContract.CommonDataKinds.Phone.NUMBER);
//                editor.putString("idPool","us-east-1:db3a6e00-7c35-4f48-b956-eaf3375a024f");
                    editor.apply();
                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("registered", true);
                    bundle.putString("loginMethod", "FB");
                    bundle.putString("profileName", profile.getName());
//                bundle.putString("idPool",preferences.getString("idPool","us-east-1:db3a6e00-7c35-4f48-b956-eaf3375a024f"));
                    i.putExtra("bundle", bundle);

                    startActivity(i);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
//                textViewFB.setText("Cancel");
                Log.v("_dan","cancel");
            }

            @Override
            public void onError(FacebookException exception) {
//                textViewFB.setText("Issue logging in to Facebook");
                Log.v("_dan","error");
            }

        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        try{
            printHash();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void printHash(){
        // code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.facebook.samples.hellofacebook",
                    "com.hhalpha.daniel.homehuntermanagerapp11",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("_dan's KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}