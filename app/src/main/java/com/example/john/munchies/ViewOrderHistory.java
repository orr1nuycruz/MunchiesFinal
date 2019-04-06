package com.example.john.munchies;

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

public class ViewOrderHistory extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    ListView historyList;
    ArrayList<String> historyArrayList;
    ArrayAdapter<String> historyAdapter;

    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    String getUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_history);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        getUser = preferences.getString("userEmail", "");

        //Firebase Database
        myFB = FirebaseDatabase.getInstance();
        //View
        historyList = (ListView)findViewById(R.id.restaurantListView);
        //
        historyArrayList = new ArrayList<String>();
        displayRestaurantList();

        historyList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String Oid = String.valueOf(parent.getItemAtPosition(position));
                myRef = myFB.getReference("MunchiesDB").child("OrderHistory").child(getUser).child(Oid);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            OrderHistoryClass post = dataSnapshot.getValue(OrderHistoryClass.class);
                            String order = post.getOrder();
                            String day= post.getCurrentDay();
                            String price = post.getPrice();
                            String status = post.getApproval();
                            Toast.makeText(getApplicationContext(),
                                    "Order: " + order + "" +
                                       "\nOrder Date: " + day + "" +
                                       "\nPrice: $" + price + "" +
                                       "\nStatus: " + status, Toast.LENGTH_LONG).show();

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return true;
            }
        });
    }

    public void displayRestaurantList(){
    //Root munchies, child restaurants
        myRef = myFB.getReference("MunchiesDB").child("OrderHistory").child(getUser);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyArrayList.clear();
                for(DataSnapshot item: dataSnapshot.getChildren()){
                    OrderHistoryClass post = item.getValue(OrderHistoryClass.class);
                    historyArrayList.add(post.getOrderNum());
                }


                historyAdapter = new ArrayAdapter<String>(ViewOrderHistory.this,
                        android.R.layout.simple_list_item_1, historyArrayList);
                historyList.setAdapter(historyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
