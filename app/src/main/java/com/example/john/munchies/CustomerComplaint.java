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
import java.util.List;

public class CustomerComplaint extends AppCompatActivity {

    EditText compName, compOrderId, compPhone, compIssue, compEmail;
    Spinner compRest;
    ArrayAdapter<String> restaurantAdapter;
    Button submitComp;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref, ref2;
    String getName, getOrderId, getPhone, getIssue, getEmail,getRest;

    int value = 1000;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_complaint);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = pref.edit();
        if (pref.contains("complaintID")) {
            String getValue = pref.getString("complaintID", null);
            value = Integer.parseInt(getValue);
        }

        compName = (EditText) findViewById(R.id.CompName);
        compOrderId = (EditText) findViewById(R.id.CompOrderId);
        compPhone = (EditText) findViewById(R.id.CompNumber);
        compIssue = (EditText) findViewById(R.id.CompIssue);
        compEmail = (EditText) findViewById(R.id.EmailText);

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

                getName = compName.getText().toString();
                getOrderId = compOrderId.getText().toString();
                getEmail = compEmail.getText().toString();
                getPhone = compPhone.getText().toString();
                getIssue = compIssue.getText().toString();
                getRest = compRest.getSelectedItem().toString();

                if (getName.equals("") || getOrderId.equals("") || getPhone.equals("") || getIssue.equals("") || getEmail.equals("")) {
                }
                else
                {
                    value += 1;
                    String getValue = Integer.toString(value);
                    //ComplaintClass newcomplaint = new ComplaintClass(getOrderId, getName, getPhone, getIssue);
                    ComplaintClass newcomplaint = new ComplaintClass(getValue, getName, getEmail, getRest, getPhone, getOrderId, getIssue);
                    ref.child("Complaints").child(getValue).setValue(newcomplaint);
                    editor.putString("complaintID", getValue);
                    editor.apply();
                    compName.setText("");
                    compOrderId.setText("");
                    compPhone.setText("");
                    compIssue.setText("");
                    compEmail.setText("");

                    Intent i = new Intent(CustomerComplaint.this, Homepage.class);
                    startActivity(i);
                }

            }
        });
    }
}

