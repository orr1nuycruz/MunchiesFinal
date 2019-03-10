package com.example.john.munchies;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

public class RestaurantMenuCRUD extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    Button btnAddMenu;
    Button btnUpdateMenu;
    Button btnDeleteMenu;

    EditText editRestaurantMenuName;
    EditText editRestaurantMenuPrice;

    String restaurantUser;
    String restaurantName;


    ListView restaurantMenuList;
    ArrayList<String> restaurantMenuArrayList;
    ArrayAdapter<String> restaurantMenuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu_crud);

        myFB = FirebaseDatabase.getInstance();
        myRef = myFB.getReference("MunchiesDB").child("RestaurantItems");

        btnAddMenu = (Button)findViewById(R.id.btnAddMenu);
        btnUpdateMenu = (Button)findViewById(R.id.btnUpdateMenu);
        btnDeleteMenu = (Button)findViewById(R.id.btnDeleteMenu);

        editRestaurantMenuName = (EditText)findViewById(R.id.EditMenuName);
        editRestaurantMenuPrice = (EditText)findViewById(R.id.EditMenuPrice);


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        restaurantUser = sharedPref.getString("User", "");
        restaurantName = sharedPref.getString("RestName", "");


        restaurantMenuList = (ListView)findViewById(R.id.listMenuItemCRUD);
        restaurantMenuArrayList = new ArrayList<String>();

        displayRestaurantList();
        addMenu();
    }

    public void addMenu(){
        btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getRestaurantItemName = editRestaurantMenuName.getText().toString();
                String getRestaurantUser = restaurantUser.toString();
                String getRestaurantName = restaurantName.toString();
                String getPrice = editRestaurantMenuPrice.getText().toString();
                Double price = Double.valueOf(getPrice).doubleValue();

                if(getRestaurantItemName.equals("") || getPrice.equals("")){
                    Toast.makeText( RestaurantMenuCRUD.this, "Please fill out all information", Toast.LENGTH_LONG).show();
                } else {
                    RestaurantItemClass menu = new RestaurantItemClass( getRestaurantItemName, getRestaurantName, price);
                    myRef.child(getRestaurantName).child(getRestaurantItemName).setValue(menu);
                    editRestaurantMenuName.setText("");
                    editRestaurantMenuPrice.setText("");
                }
            }
        });
    }


    public void deleteRestaurant(){
        final RestaurantItemClass restaurant = new RestaurantItemClass();
        restaurantMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                restaurant.setItemID(restaurantMenuArrayList.get(position));
            }
        });

        btnDeleteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = restaurant.getItemID();
                if(name.equals("")){
                    Toast.makeText( RestaurantMenuCRUD.this, "Please Select item before delete!", Toast.LENGTH_LONG).show();
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


    //Display the restaurant name from the database
    public void displayRestaurantList(){
        myRef = myFB.getReference("MunchiesDB").child("RestaurantItems").child(restaurantName);
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
        restaurantMenuArrayList.clear();
        for(DataSnapshot item: dataSnapshot.getChildren()){
            RestaurantItemClass restaurant = item.getValue(RestaurantItemClass .class);
            restaurantMenuArrayList.add(restaurant.getItemName() + " " + restaurant.getPrice());
        }


        restaurantMenuAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, restaurantMenuArrayList);
        restaurantMenuList.setAdapter(restaurantMenuAdapter);

    }
}
