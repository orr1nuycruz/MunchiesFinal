package com.example.john.munchies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ComplaintList extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef, myRef2;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;

    String getUser, getRestName;

    ListView complaintList;
    ArrayList<String> complaintArrayList;
    ArrayAdapter<String> complaintAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_list);

        //Firebase Database
        myFB = FirebaseDatabase.getInstance();

        complaintList = (ListView)findViewById(R.id.complaintListView);
        complaintArrayList  = new ArrayList<String>();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();

        getUser = sharedPref.getString("userEmail", "");
        getRestName = sharedPref.getString("RestName", "");

        displayComplaintList();
        getComplaintdetails();

    }

    public void displayComplaintList(){
        myRef = myFB.getReference("MunchiesDB").child("Complaints");
        myRef2 = myFB.getReference("MunchiesDB").child("RestComplaints");

        if(!getUser.isEmpty() && getUser.equals("admin")){
            myRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        complaintArrayList.clear();
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            ComplaintClass child = item.getValue(ComplaintClass.class);
                            complaintArrayList.add(child.getID());
                        }

                        complaintAdapter = new ArrayAdapter<String>(ComplaintList.this,
                                android.R.layout.simple_list_item_1, complaintArrayList);
                        complaintList.setAdapter(complaintAdapter);
                    }
                    else{
                        Toast.makeText(ComplaintList.this, "no data", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if(!getRestName.isEmpty()){
            myRef.orderByChild("restaurant").equalTo(getRestName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        complaintArrayList.clear();
                        //Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            ComplaintClass child = item.getValue(ComplaintClass.class);
                            complaintArrayList.add(child.getID());
                        }

                        complaintAdapter = new ArrayAdapter<String>(ComplaintList.this,
                                android.R.layout.simple_list_item_1, complaintArrayList);
                        complaintList.setAdapter(complaintAdapter);
                    }
                    else{
                        Toast.makeText(ComplaintList.this, "no data", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }



    public void getComplaintdetails(){
        complaintList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the position
                String ID = String.valueOf(complaintList.getItemAtPosition(position));
                //Get Placement
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                //Get the string
                editor.putString("restComp", ID);
                //Save
                editor.apply();
                if(!getUser.isEmpty() && getUser.equals("admin")) {
                    Intent myIntent = new Intent(ComplaintList.this, ShowComplaintsToAdmin.class);
                    startActivity(myIntent);
                }
                else if (!getRestName.isEmpty()){
                    Intent myIntent = new Intent(ComplaintList.this, ShowComplaintsToRestaurant.class);
                    startActivity(myIntent);
                }
            }
        });
    }

}
