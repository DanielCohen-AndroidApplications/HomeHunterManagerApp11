package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Daniel on 5/30/2016.
 */
public class ConfirmActivity extends Activity implements OnMapReadyCallback {
    String address, shortBlurb, bigBlurb, payPeriod, minSalary,beds, baths, sqft, rent;

    Boolean guarantor, dogs, smallDogs, cats, couples, children, smoking, securityDeposit, doorman;
    TextView textViewAddress,textViewSqft,textViewRent;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bundle=getIntent().getBundleExtra("bundle");
        try {
//            Log.v("_dan", bundle.getStringArrayList("arrayList").get(0));
//            Log.v("_dan", bundle.getStringArrayList("arrayList").get(1));
            ArrayList<String> arrayList = bundle.getStringArrayList("arrayList");
            for(int i=0;i<arrayList.size();i++){
                Log.v("_dan",arrayList.get(i));
            }
        }catch (Exception e ){
            Log.v("_dan", e.getMessage());
        }
//        textViewAddress=(TextView) findViewById(R.id.textViewAddress);
//        textViewSqft=(TextView) findViewById(R.id.textViewSqft);
//        textViewRent=(TextView) findViewById(R.id.textViewRent);
//        try{
//            extras=getIntent().getExtras();
//            address=extras.getString("address");
//            textViewAddress.setText(address);
//            sqft=extras.getString("sqft");
//            textViewSqft.setText(sqft);
//            rent=extras.getString("rent");
//            textViewRent.setText(rent);
////            address=getIntent().getExtras().getString("address");
////            sqft=getIntent().getExtras().getDouble("sqft",0);
////            beds=getIntent().getExtras().getDouble("beds",0);
////            baths=getIntent().getExtras().getDouble("baths",0);
////            rent=getIntent().getExtras().getInt("rent",0);
////            minSalary=getIntent().getExtras().getString("minSalary");
////            shortBlurb=getIntent().getExtras().getString("shortBlurb");
////            bigBlurb=getIntent().getExtras().getString("bigBlurb");
////            couples=getIntent().getExtras().getBoolean("couples");
////            children=getIntent().getExtras().getBoolean("children");
////            smallDogs=getIntent().getExtras().getBoolean("smallDogs");
////            dogs=getIntent().getExtras().getBoolean("dogs");
////            cats=getIntent().getExtras().getBoolean("cats");
////            smoking=getIntent().getExtras().getBoolean("smoking");
////            guarantor=getIntent().getExtras().getBoolean("guarantor");
////            securityDeposit=getIntent().getExtras().getBoolean("securityDeposit");
////            doorman=getIntent().getExtras().getBoolean("doorman");
//
//        }catch (Exception e){
//            Log.v("_dan Confirm",e.getMessage());
//        }
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
}
