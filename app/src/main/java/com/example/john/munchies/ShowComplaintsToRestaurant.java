package com.example.john.munchies;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowComplaintsToRestaurant extends AppCompatActivity {

    TextView getOrderID, getName, getEmail, getPhone, getIssue;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;

    String getID;
    String StrOrderId, StrName, StrEmail, StrPhone,  StrIssue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_complaints_to_restaurant);

        getOrderID = (TextView) findViewById(R.id.showOrderID);
        getName = (TextView) findViewById(R.id.showName);
        getEmail = (TextView) findViewById(R.id.showEmail);
        getPhone = (TextView) findViewById(R.id.showPhone);
        getIssue = (TextView) findViewById(R.id.showIssue);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();
        getID = sharedPref.getString("restComp", "");

        firebaseDatabase = FirebaseDatabase.getInstance();
        displayContent();
    }

    private void displayContent(){

        ref = firebaseDatabase.getReference("MunchiesDB").child("Complaints").child(getID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ComplaintClass post = dataSnapshot.getValue(ComplaintClass.class);
                    StrOrderId = post.getOrderId();
                    StrName = post.getName();
                    StrEmail= post.getEmail();
                    StrPhone = post.getPhone();
                    StrIssue = post.getIssue();
                    getOrderID.setText(StrOrderId);
                    getName.setText(StrName);
                    getEmail.setText(StrEmail);
                    getPhone.setText(StrPhone);
                    getIssue.setText(StrIssue);
                    Toast.makeText(getApplicationContext(), "theres data", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Data not found: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }
}
