package com.example.john.munchies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestauantComplaint extends AppCompatActivity {


    EditText compPhone, compSubject, compIssue;
    Spinner compRest;
    ArrayAdapter<String> restaurantAdapter;
    Button submitComp;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    String getPhone, getIssue, getSubject ,getRest;

    int value = 1000;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restauant_complaint);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = pref.edit();
        if (pref.contains("complaintRestID")) {
            String getValue = pref.getString("complaintRestID", null);
            value = Integer.parseInt(getValue);
        }

        compPhone = (EditText) findViewById(R.id.CompNumber);
        compIssue = (EditText) findViewById(R.id.CompIssue);
        compSubject =(EditText) findViewById(R.id.CompSubject);


        compRest = (Spinner) findViewById(R.id.CompRestaurant);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("MunchiesDB");

        //Populate Spinner with Restarant Names
        ref.child("Restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    showData(dataSnapshot);
                }
                else{
                    Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submitComp = (Button) findViewById(R.id.btnSubmitComp);


        //Make customer complaint
        complaint();


    }

    public void showData(DataSnapshot dataSnapshot){
        final ArrayList<String> restaurantList = new ArrayList<String>();
        for (DataSnapshot restaurantVals : dataSnapshot.getChildren()){
            RestaurantClass restItem = restaurantVals.getValue(RestaurantClass.class);
            String name = restItem.getRestaurantName();
            restaurantList.add(name);
        }
        restaurantAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, restaurantList);
        restaurantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        compRest.setAdapter(restaurantAdapter);

    }

    public void complaint(){
        submitComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSubject = compSubject.getText().toString();
                getPhone = compPhone.getText().toString();
                getIssue = compIssue.getText().toString();
                getRest = compRest.getSelectedItem().toString();

                if (getSubject.equals("") || getPhone.equals("") || getIssue.equals("")){
                }
                else
                {
                    value += 1;
                    String getValue = Integer.toString(value);
//                    ComplaintClass newcomplaint = new ComplaintClass(getOrderId, getName, getPhone, getIssue);
                    ComplaintClass newcomplaint = new ComplaintClass(getValue, getRest,getPhone,getSubject,getIssue);
                    ref.child("RestComplaints").child(getValue).setValue(newcomplaint);
                    editor.putString("complaintRestID", getValue);
                    editor.apply();
                    compSubject.setText("");
                    compPhone.setText("");
                    compIssue.setText("");


                    Intent i = new Intent(RestauantComplaint.this , Homepage.class);
                    startActivity(i);
                }

            }
        });
    }
}


