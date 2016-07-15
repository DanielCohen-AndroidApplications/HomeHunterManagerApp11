package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by Daniel on 5/30/2016.
 */
public class SplashActivity extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    Boolean registered;
    String profileName, loginMethod;
    Bundle bundle;
    Intent i;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        bundle=new Bundle();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        try{
            Log.v("_dan",preferences.getAll().toString());

            registered = preferences.getBoolean("registered", false);
            profileName = preferences.getString("profileName", "");
            loginMethod = preferences.getString("loginMethod", "");
            bundle.putBoolean("registered", registered);
            bundle.putString("loginMethod", loginMethod);
            bundle.putString("profileName", profileName);
            i = new Intent(SplashActivity.this, MainActivity.class);
            i.putExtra("bundle",bundle);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(registered.equals(false)||profileName.isEmpty()||profileName.equals("")||loginMethod.isEmpty()||loginMethod.equals("")){
            i = new Intent(SplashActivity.this, RegisterActivity.class);
        }

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
//                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
