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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class    RestaurantMenu extends AppCompatActivity {

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    String restaurantName;
    String userEmail, menuItem;
    ArrayList<String> orderItems;
   Double orderPrice;


    RestaurantItemClass restaurant;
    ListView restaurantMenuList;
    Date currentDay;
    Set<String> set;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    ArrayList<String> restaurantMenuArrayList;
    ArrayAdapter<String> restaurantMenuAdapter;

    Button btnAddToMyCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        //Shared
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPref.edit();
        set  = new HashSet<String>();
        restaurantName = sharedPref.getString("RestaurantName", "");
        userEmail = sharedPref.getString("userEmail", "");


        orderItems = new ArrayList<String>();
        orderPrice = 0.0;
        //FireBase
        myFB = FirebaseDatabase.getInstance();
        myRef = myFB.getReference("MunchiesDB");

        btnAddToMyCart = (Button)findViewById(R.id.btnAddToMyCart);

        restaurantMenuList = (ListView)findViewById(R.id.restaurantMenuListView);
        restaurantMenuArrayList = new ArrayList<String>();

        displayRestaurantList();
        addItems();
    }

    //Display the restaurant name from the database
    public void displayRestaurantList(){

        //Get the restaurant Items under restaurant name
        myRef = myFB.getReference("MunchiesDB").child("RestaurantItems").child(restaurantName);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Shos data
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addItems() {
        restaurantMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Get the values, and split the prices
                //got the position
                menuItem = String.valueOf(adapterView.getItemAtPosition(position));
                String[] parts = menuItem.split("\n"); //Array, each element is text between a dash
                double parsePrice = Double.parseDouble(parts[1]);

                if (restaurantMenuList.isItemChecked(position)){
                    orderItems.add(menuItem);
                    orderPrice += parsePrice;

                    Toast.makeText(RestaurantMenu.this, "Added " + menuItem, Toast.LENGTH_SHORT).show();

                }
                else{
                    orderItems.remove(menuItem);
                    orderPrice -= parsePrice;

                    Toast.makeText(RestaurantMenu.this, "Removed " + menuItem, Toast.LENGTH_SHORT).show();
                }

            }

        });
                //Create a firebase for the order


                //I have to add it to my shared preferences
                //Then start the next Activity
        btnAddToMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set.addAll(orderItems);
                editor.putString("orderPrice", Double.toString(orderPrice));
                editor.putStringSet("order", set);
                editor.commit();
                nextPage();
            }
        });
    }


    public void showData(DataSnapshot dataSnapshot){
        restaurantMenuArrayList.clear();
        for(DataSnapshot item: dataSnapshot.getChildren()){
            //Innitiate class - get the value of the item class

            //DOES NOT KNOW HOW THIS WORKS
            restaurant = item.getValue(RestaurantItemClass .class);
            //Add it to our Arraylist

            restaurantMenuArrayList.add(restaurant.getItemName() + "\n" + restaurant.getPrice());



        }


        //Adapter
        restaurantMenuAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, restaurantMenuArrayList);

        //Last, add to list view

        restaurantMenuList.setAdapter(restaurantMenuAdapter);

    }




    public void addMenuToMyCart(){
        btnAddToMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                restaurantMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        RestaurantItemClass item = new RestaurantItemClass();
                        //get the values
                        String getUserEmail = userEmail;
                        String getRestaurant = restaurantName;
                        //nothing in there???
                        String getItemName = item.getItemName();
                        Double Price = Double.valueOf(item.getPrice()).doubleValue();
                        String toast = String.valueOf(restaurantMenuList.getItemAtPosition(position));
                        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
                        if(getItemName.equals("")){
                            Toast.makeText( RestaurantMenu.this, "Please fill out all information", Toast.LENGTH_LONG).show();
                        } else {
                            CustomerCartClass menu = new CustomerCartClass( getUserEmail, getRestaurant, getItemName, Price);
                            myRef.child(getUserEmail).setValue(menu);
                        }
                    }
                });
            }
        });
    }

    public void nextPage(){
        Intent i = new Intent(this, RestaurantCustomerOrder.class);
        startActivity(i);

    }

}
