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

public class ShowComplaintsToAdmin extends AppCompatActivity {

    TextView getRest, getPhone, getSub, getIssue;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;

    String getID;
    String StrRest, StrPhone, StrSub, StrIssue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_complaints_to_admin);

        getRest = (TextView) findViewById(R.id.showRestaurant);
        getPhone = (TextView) findViewById(R.id.showPhone);
        getSub = (TextView) findViewById(R.id.showSubject);
        getIssue = (TextView) findViewById(R.id.showIssue);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();
        getID = sharedPref.getString("restComp", "");

        firebaseDatabase = FirebaseDatabase.getInstance();

        displayContent();
    }

    private void displayContent(){

        ref = firebaseDatabase.getReference("MunchiesDB").child("RestComplaints").child(getID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ComplaintClass post = dataSnapshot.getValue(ComplaintClass.class);
                    StrRest = post.getRestaurant();
                    StrPhone = post.getPhone();
                    StrSub = post.getSubject();
                    StrIssue = post.getIssue();
                    getRest.setText(StrRest);
                    getPhone.setText(StrPhone);
                    getSub.setText(StrSub);
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
