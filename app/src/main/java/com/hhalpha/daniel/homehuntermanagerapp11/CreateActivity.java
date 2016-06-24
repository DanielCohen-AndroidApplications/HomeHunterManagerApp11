package com.hhalpha.daniel.homehuntermanagerapp11;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import java.util.Arrays;

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
    ArrayList<Boolean> booleans;
    File pic1, pic2, pic3, pic4;
    String path1, path2, path3, path4;
    Uri uri1, uri2, uri3, uri4;
    ArrayList<String> editArrayList;
    Bundle bundle;
    Boolean editing;
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
        editTextBed=(EditText) findViewById(R.id.editTextBed);
        editTexts.add(editTextBed);
        editTextBath=(EditText) findViewById(R.id.editTextBath);
        editTexts.add(editTextBath);
        submitBtn=(Button) findViewById(R.id.submitBtn);
        numberPics=1;
        spinnerPayPeriods = (Spinner) findViewById(R.id.spinnerPayPeriod);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pay_periods, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerPayPeriods.setAdapter(adapter);
        final CheckBox checkBoxCouples = (CheckBox) findViewById(R.id.checkBoxCouples);
        final CheckBox checkBoxChildren = (CheckBox) findViewById(R.id.checkBoxChildren);
        final CheckBox checkBoxSmallDogs = (CheckBox) findViewById(R.id.checkBoxSmallDogs);
        final CheckBox checkBoxDogs = (CheckBox) findViewById(R.id.checkBoxDogs);
        final CheckBox checkBoxCats = (CheckBox) findViewById(R.id.checkBoxCats);
        final CheckBox checkBoxSmoking = (CheckBox) findViewById(R.id.checkBoxSmoking);
        final CheckBox checkBoxGuarantor = (CheckBox) findViewById(R.id.checkBoxGuarantor);
        final CheckBox checkBoxDeposit = (CheckBox) findViewById(R.id.checkBoxDeposit);
        final CheckBox checkBoxDoorman = (CheckBox) findViewById(R.id.checkBoxDoorman);
        try{
            bundle = getIntent().getBundleExtra("bundle");
            editing = bundle.getBoolean("editing");
            if(editing){
                Log.v("_dan edit arrayList",bundle.getStringArrayList("arrayList").toString() );
                editArrayList=new ArrayList<String>(Arrays.asList(bundle.getStringArrayList("arrayList").toString().split(",")));
                Log.v("_dan editarrayList",editArrayList.toString());
                editTextLocation.setText(editArrayList.get(0).replace("[","").replace("+",","));
                editTextSqft.setText(editArrayList.get(2));
                editTextRent.setText(editArrayList.get(3));
                editTextMinSalary.setText(editArrayList.get(4));
                editTextShort.setText(editArrayList.get(5));
                editTextLong.setText(editArrayList.get(6));
                editTextBed.setText(editArrayList.get(7));
                editTextBath.setText(editArrayList.get(8));
                checkBoxCouples.setChecked(Boolean.parseBoolean(editArrayList.get(9).replace(" ","")));
                checkBoxChildren.setChecked(Boolean.parseBoolean(editArrayList.get(10).replace(" ","")));
                checkBoxSmallDogs.setChecked(Boolean.parseBoolean(editArrayList.get(11).replace(" ","")));
                checkBoxDogs.setChecked(Boolean.parseBoolean(editArrayList.get(12).replace(" ","")));
                checkBoxCats.setChecked(Boolean.parseBoolean(editArrayList.get(13).replace(" ","")));
                checkBoxSmoking.setChecked(Boolean.parseBoolean(editArrayList.get(14).replace(" ","")));
                checkBoxGuarantor.setChecked(Boolean.parseBoolean(editArrayList.get(15).replace(" ","")));
                checkBoxDeposit.setChecked(Boolean.parseBoolean(editArrayList.get(16).replace(" ","")));
                checkBoxDoorman.setChecked(Boolean.parseBoolean(editArrayList.get(17).replace(" ","")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
                                pic1 = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES), selectedImage.toString());
                                pic1.createNewFile();
                                path1=selectedImage.toString();
                                uri1=imageUri;
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
                                pic2 = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES), selectedImage.toString());
                                pic2.createNewFile();
                                path2=pic2.getPath();
                                uri2=imageUri;
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
                                pic3 = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES), selectedImage.toString());
                                pic3.createNewFile();
                                path3=pic3.getPath();
                                uri3=imageUri;
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
                                pic4 = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES), selectedImage.toString());
                                pic4.createNewFile();
                                path4=pic4.getPath();
                                uri4=imageUri;
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
        final CheckBox checkBoxCouples = (CheckBox) findViewById(R.id.checkBoxCouples);
        final CheckBox checkBoxChildren = (CheckBox) findViewById(R.id.checkBoxChildren);
        final CheckBox checkBoxSmallDogs = (CheckBox) findViewById(R.id.checkBoxSmallDogs);
        final CheckBox checkBoxDogs = (CheckBox) findViewById(R.id.checkBoxDogs);
        final CheckBox checkBoxCats = (CheckBox) findViewById(R.id.checkBoxCats);
        final CheckBox checkBoxSmoking = (CheckBox) findViewById(R.id.checkBoxSmoking);
        final CheckBox checkBoxGuarantor = (CheckBox) findViewById(R.id.checkBoxGuarantor);
        final CheckBox checkBoxDeposit = (CheckBox) findViewById(R.id.checkBoxDeposit);
        final CheckBox checkBoxDoorman = (CheckBox) findViewById(R.id.checkBoxDoorman);
        Bundle bundle = new Bundle();
        ArrayList<String> arrayList = new ArrayList<String>();
        booleans=new ArrayList<Boolean>();
        if (checkBoxCouples.isChecked()) {
            couples = true;
        } else {
            couples = false;
        }
        booleans.add(couples);
        if (checkBoxChildren.isChecked()) {
            children = true;
        } else {
            children = false;
        }
        booleans.add(children);
        if (checkBoxSmallDogs.isChecked()) {
            smallDogs = true;
        } else {
            smallDogs = false;
        }
        booleans.add(smallDogs);
        if (checkBoxDogs.isChecked()) {
            dogs = true;
        } else {
            dogs = false;
        }
        booleans.add(dogs);

        if (checkBoxCats.isChecked()) {
            cats = true;
        } else {
            cats = false;
        }
        booleans.add(cats);

        if (checkBoxSmoking.isChecked()) {
            smoking = true;
        } else {
            smoking = false;
        }
        booleans.add(smoking);

        if (checkBoxGuarantor.isChecked()) {
            guarantor = true;
        } else {
            guarantor = false;
        }
        booleans.add(guarantor);
        if (checkBoxDeposit.isChecked()) {
            securityDeposit = true;
        } else {
            securityDeposit = false;
        }
        booleans.add(securityDeposit);
        if (checkBoxDoorman.isChecked()) {
            doorman = true;
        } else {
            doorman = false;
        }
        booleans.add(doorman);

        arrayList.add(editTextLocation.getText().toString());
        for(int i=0;i<editTexts.size();i++){
            arrayList.add(editTexts.get(i).getText().toString());
        }
        try {
            for (int i = 0; i < booleans.size(); i++) {
                arrayList.add(booleans.get(i).toString());
            }
        }catch (Exception e){
            Log.v("_dan", e.getMessage());
        }
        arrayList.add(uri1.toString());
        arrayList.add(uri2.toString());
        arrayList.add(uri3.toString());
        arrayList.add(uri4.toString());
        arrayList.add(path1.toString());
        arrayList.add(path2.toString());
        arrayList.add(path3.toString());
        arrayList.add(path4.toString());
        bundle.putStringArrayList("arrayList",arrayList);
        bundle.putBoolean("firstTime",true);
        //TODO:Handle situations where fields are left blank/less than 3 photos are added
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
