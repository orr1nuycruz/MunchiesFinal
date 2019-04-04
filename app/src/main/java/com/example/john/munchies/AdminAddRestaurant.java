package com.example.john.munchies;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminAddRestaurant extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    EditText editRestaurantName;
    EditText editRestaurantID;

    Button btnAddRestaurant;
    Button btnDeleteRestaurant;


    ListView restaurantList;
    ArrayList<String> restaurantArrayList;
    ArrayAdapter<String> restaurantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_restaurant);

        myFB = FirebaseDatabase.getInstance();
        myRef = myFB.getReference("MunchiesDB");
        editRestaurantName = (EditText)findViewById(R.id.editAdminAddRestaurant);
        editRestaurantID = (EditText)findViewById(R.id.editAdminAddResID);
        btnAddRestaurant = (Button)findViewById(R.id.AddRestaurant);
        btnDeleteRestaurant = (Button)findViewById(R.id.DeleteRestaurant);
        btnDeleteRestaurant.setEnabled(false);
        addRestaurant();

        restaurantList = (ListView)findViewById(R.id.restaurantListView);
        restaurantArrayList = new ArrayList<String>();

        displayRestaurantList();
        deleteRestaurant();

    }

    public void displayRestaurantList(){

        myRef = myFB.getReference("MunchiesDB").child("Restaurants");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showData(DataSnapshot dataSnapshot){
        restaurantArrayList.clear();
        for(DataSnapshot item: dataSnapshot.getChildren()){
            RestaurantClass restaurant = item.getValue(RestaurantClass.class);
            restaurantArrayList.add(restaurant.getRestaurantName());
        }


        restaurantAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, restaurantArrayList);
        restaurantList.setAdapter(restaurantAdapter);

    }

    public void addRestaurant(){
        btnAddRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getRestaurant = editRestaurantName.getText().toString();
                String getID = editRestaurantID.getText().toString();
                if(getRestaurant.equals("") || getID.equals("")){
                    Toast.makeText( AdminAddRestaurant.this, "Please fill out all information", Toast.LENGTH_LONG).show();
                } else {
                    RestaurantClass restaurant = new RestaurantClass(getID, getRestaurant);
                    myRef.child(getRestaurant).setValue(restaurant);
                    editRestaurantName.setText("");
                    editRestaurantID.setText("");
                }
            }
        });

    }

    public void deleteRestaurant(){
        final RestaurantClass restaurant = new RestaurantClass();
        restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                restaurant.setRestaurantID(restaurantArrayList.get(position));
                editRestaurantName.setText(restaurantArrayList.get(position));

                //Enable or Disable Buttons
                btnDeleteRestaurant.setEnabled(true);
                btnAddRestaurant.setEnabled(false);

                //Disable editing
                editRestaurantName.setEnabled(false);
                editRestaurantID.setEnabled(false);


            }
        });

        btnDeleteRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Enable or Disable Buttons
                btnDeleteRestaurant.setEnabled(false);
                btnAddRestaurant.setEnabled(true);

                //Enable Editing
                editRestaurantName.setEnabled(true);
                editRestaurantID.setEnabled(true);

                //Clear Editing
                editRestaurantName.setText("");
                editRestaurantID.setText("");
                final String name = restaurant.getRestaurantID();
                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                if(name.equals("")){
                    Toast.makeText( AdminAddRestaurant.this, "Please Select item before delete!", Toast.LENGTH_LONG).show();
                } else {
                    myRef.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            myRef.child(name).removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });
    }
}
