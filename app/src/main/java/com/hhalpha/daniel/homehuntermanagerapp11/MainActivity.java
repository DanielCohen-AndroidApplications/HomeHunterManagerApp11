package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Daniel on 5/30/2016.
 */
public class MainActivity extends Activity {
    Button listBtn, createBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listBtn=(Button) findViewById(R.id.listBtn);
        createBtn=(Button) findViewById(R.id.createBtn);
    }

    public void listProperties(View v){

    }

    public void createNewProperty(View v){
        Intent i= new Intent(MainActivity.this, CreateActivity.class);
        startActivity(i);
    }

}
