package com.example.john.munchies;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_complaint);
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

                }


            }
        });
    }}