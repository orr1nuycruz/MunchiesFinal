package com.example.john.munchies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerComplaint extends AppCompatActivity {

    EditText compName, compOrderId, compPhone, compIssue;
    Button submitComp;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref, ref2;
    String getName, getOrderId, getPhone, getIssue;

    int value = 1000;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_complaint);
        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = pref.edit();
        if(pref.contains("complaintID")){
            String getValue = pref.getString("complaintID", null);
            value = Integer.parseInt(getValue);
        }

        compName = (EditText) findViewById(R.id.CompName);
        compOrderId = (EditText) findViewById(R.id.CompOrderId);
        compPhone = (EditText) findViewById(R.id.CompNumber);
        compIssue = (EditText) findViewById(R.id.CompIssue);

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("MunchiesDB");

        submitComp = (Button) findViewById(R.id.btnSubmitComp);


        submitComp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getName = compName.getText().toString();
                getOrderId = compOrderId.getText().toString();
                getPhone = compPhone.getText().toString();
                getIssue = compIssue.getText().toString();

                if (getName.equals("") || getOrderId.equals("") || getPhone.equals("") || getIssue.equals("")) {
                }
                else
                {
                    value += 1;
                    String getValue = Integer.toString(value);
                    ComplaintClass newcomplaint = new ComplaintClass(getOrderId, getName, getPhone, getIssue);
                    ref.child("Complaints").child(getValue).setValue(newcomplaint);
                    editor.putString("complaintID", getValue);
                    editor.apply();
                    compName.setText("");
                    compOrderId.setText("");
                    compPhone.setText("");
                    compIssue.setText("");

                    Intent i = new Intent(CustomerComplaint.this, Homepage.class);
                    startActivity(i);
                }


            }
        });
    }}