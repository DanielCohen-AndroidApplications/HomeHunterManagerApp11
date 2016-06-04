package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Daniel on 5/30/2016.
 */
public class CreateActivity extends Activity {
    private final int SELECT_PHOTO = 1;
    Button picsBtn, submitBtn;
    int numberPics;
    ImageView imageView;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    EditText editTextLocation, editTextSqft,editTextRent,editTextMinSalary, editTextShort,editTextLong,editTextBed, editTextBath;
    ArrayList<EditText> editTexts;
    Spinner spinnerPayPeriods;
    CheckBox checkBoxCouples, checkBoxChildren, checkBoxSmallDogs, checkBoxDogs, checkBoxCats, checkBoxSmoking, checkBoxGuarantor, checkBoxDeposit, checkBoxDoorman;
    String address, shortBlurb, bigBlurb, payPeriod, minSalary,beds, baths, sqft, rent;

    Boolean guarantor, dogs, smallDogs, cats, couples, children, smoking, securityDeposit, doorman;
    File pic1, pic2, pic3, pic4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        picsBtn=(Button) findViewById(R.id.picsBtn);
        editTextLocation=(EditText) findViewById(R.id.editTextLocation);
        editTexts=new ArrayList<EditText>();
        editTexts.add(editTextLocation);
        editTextSqft=(EditText) findViewById(R.id.editTextSqft);
        editTexts.add(editTextSqft);
        editTextRent=(EditText) findViewById(R.id.editTextRent);
        editTexts.add(editTextRent);
        editTextMinSalary=(EditText) findViewById(R.id.editTextMinSalary);
        editTexts.add(editTextMinSalary);
        editTextShort=(EditText) findViewById(R.id.editTextShort);
        editTexts.add(editTextShort);
        editTextLong=(EditText) findViewById(R.id.editTextLong);
        editTexts.add(editTextLong);
        submitBtn=(Button) findViewById(R.id.submitBtn);
        numberPics=1;
        spinnerPayPeriods = (Spinner) findViewById(R.id.spinnerPayPeriod);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pay_periods, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerPayPeriods.setAdapter(adapter);
        checkBoxCouples = (CheckBox) findViewById(R.id.checkBoxCouples);
        checkBoxChildren = (CheckBox) findViewById(R.id.checkBoxChildren);
        checkBoxSmallDogs = (CheckBox) findViewById(R.id.checkBoxSmallDogs);
        checkBoxDogs = (CheckBox) findViewById(R.id.checkBoxDogs);
        checkBoxCats = (CheckBox) findViewById(R.id.checkBoxCats);
        checkBoxSmoking = (CheckBox) findViewById(R.id.checkBoxSmoking);
        checkBoxGuarantor = (CheckBox) findViewById(R.id.checkBoxGuarantor);
        checkBoxDeposit = (CheckBox) findViewById(R.id.checkBoxDeposit);
        checkBoxDoorman = (CheckBox) findViewById(R.id.checkBoxDoorman);
    }

    public void selectPics(View v){
        if (numberPics < 5) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();

                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100 /*ignored for PNG*/, bos);
                        byte[] bitmapdata = bos.toByteArray();
                        if(numberPics==1) {
                            imageView.setImageBitmap(selectedImage);
                            numberPics++;

                            try{
                                pic1 = new File(getApplicationContext().getCacheDir(), selectedImage.toString());
                                pic1.createNewFile();
                            }catch(Exception e){
                                Log.v("_dan create img1", e.getMessage());
                            }
                            FileOutputStream fos = new FileOutputStream(pic1);
                            try {
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();
                            }catch(Exception e){
                                Log.v("_dan fos1", e.getMessage());
                            }
                        }else if(numberPics==2){
                            imageView2.setImageBitmap(selectedImage);
                            numberPics++;

                            try{
                                pic2 = new File(getApplicationContext().getCacheDir(), selectedImage.toString());
                                pic2.createNewFile();
                            }catch(Exception e){
                                Log.v("_dan create img2", e.getMessage());
                            }
                            FileOutputStream fos = new FileOutputStream(pic2);
                            try {
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();
                            }catch(Exception e){
                                Log.v("_dan fos2", e.getMessage());
                            }
                        }else if(numberPics==3){
                            imageView3.setImageBitmap(selectedImage);
                            numberPics++;

                            try{
                                pic3 = new File(getApplicationContext().getCacheDir(), selectedImage.toString());
                                pic3.createNewFile();
                            }catch(Exception e){
                                Log.v("_dan create img3", e.getMessage());
                            }
                            FileOutputStream fos = new FileOutputStream(pic3);
                            try {
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();
                            }catch(Exception e){
                                Log.v("_dan fos3", e.getMessage());
                            }
                        }else if(numberPics==4){
                            imageView4.setImageBitmap(selectedImage);
                            numberPics++;
                            picsBtn.setText("You've reached the maximum 4 pics");

                            try{
                                pic4 = new File(getApplicationContext().getCacheDir(), selectedImage.toString());
                                pic4.createNewFile();
                            }catch(Exception e){
                                Log.v("_dan create img4", e.getMessage());
                            }
                            FileOutputStream fos = new FileOutputStream(pic4);
                            try {
                                fos.write(bitmapdata);
                                fos.flush();
                                fos.close();
                            }catch(Exception e){
                                Log.v("_dan fos4", e.getMessage());
                            }
                        }



//                        TransferObserver observer = transferUtility.upload(
//                                "hhproperties/prop1",           // The S3 bucket to upload to
//                                "hij",          // The key for the uploaded object
//                                f// The location of the file to be uploaded
//                        );
//                        observer.refresh();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    public void submitInfo(View v){
        Bundle bundle = new Bundle();
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("hey dan");
        arrayList.add(editTextLocation.getText().toString());
        for(int i=0;i<editTexts.size();i++){
            arrayList.add(editTexts.get(i).getText().toString());
        }
        bundle.putStringArrayList("arrayList",arrayList);

// try {
//            address = editTextLocation.getText().toString();
//            sqft = editTextSqft.getText().toString();
//            rent = editTextRent.getText().toString();
//            minSalary = editTextMinSalary.getText().toString();
//            shortBlurb = editTextShort.getText().toString();
//            bigBlurb = editTextLong.getText().toString();
//            beds = editTextBed.getText().toString();
//            baths = editTextBath.getText().toString();
//
//            if (checkBoxCouples.isChecked()) {
//                couples = true;
//            } else {
//                couples = false;
//            }
//            if (checkBoxChildren.isChecked()) {
//                children = true;
//            } else {
//                children = false;
//            }
//            if (checkBoxSmallDogs.isChecked()) {
//                smallDogs = true;
//            } else {
//                smallDogs = false;
//            }
//            if (checkBoxDogs.isChecked()) {
//                dogs = true;
//            } else {
//                dogs = false;
//            }
//
//            if (checkBoxCats.isChecked()) {
//                cats = true;
//            } else {
//                cats = false;
//            }
//
//            if (checkBoxSmoking.isChecked()) {
//                smoking = true;
//            } else {
//                smoking = false;
//            }
//
//            if (checkBoxGuarantor.isChecked()) {
//                guarantor = true;
//            } else {
//                guarantor = false;
//            }
//            if (checkBoxDeposit.isChecked()) {
//                securityDeposit = true;
//            } else {
//                securityDeposit = false;
//            }
//            if (checkBoxDoorman.isChecked()) {
//                doorman = true;
//            } else {
//                doorman = false;
//            }
//        }catch(Exception e){
//            Log.v("dan_1", e.getMessage());
//        }
        Intent i = new Intent (CreateActivity.this, ConfirmActivity.class);
        i.putExtra("bundle",bundle);
//        try {
//            i.putExtra("address", address);
//            i.putExtra("sqft", sqft);
//            i.putExtra("rent",rent);
//            i.putExtra("minSalary", minSalary);
//            i.putExtra("shortBlurb", shortBlurb);
//            i.putExtra("bigBlurb", bigBlurb);
//            i.putExtra("beds", beds);
//            i.putExtra("baths", baths);
//            i.putExtra("couples", couples);
//            i.putExtra("children", children);
//            i.putExtra("smallDogs", smallDogs);
//            i.putExtra("dogs", dogs);
//            i.putExtra("cats", cats);
//            i.putExtra("smoking", smoking);
//            i.putExtra("guarantor", guarantor);
//            i.putExtra("securityDeposit", securityDeposit);
//            i.putExtra("doorman", doorman);
//
//        }catch (Exception e){
//            Log.v("_dan2",e.getMessage());
//        }
        startActivity(i);
    }
}
