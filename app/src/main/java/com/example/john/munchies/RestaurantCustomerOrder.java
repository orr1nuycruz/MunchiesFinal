package com.example.john.munchies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RestaurantCustomerOrder extends AppCompatActivity implements View.OnClickListener {

    ListView order;
    TextView price;
    Button checkout_Btn, remove_btn;
    List<String> sample;
    String userEmail;
    SimpleDateFormat day, hour;

    String currentDay, orderitem, currentHour;

    String orderPrice, restaurantName, finalPrice;
    double doubleOrderPrice;

    private ArrayAdapter<String> orderItemsAdapter;
    Set<String> setOrder;

    FirebaseDatabase myFB;
    DatabaseReference myRef, myRef2;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_customer_order);
        // Shared Preferences

        pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = pref.edit();
        setOrder = pref.getStringSet("order",null);
        orderPrice = pref.getString("orderPrice", null);
        userEmail = pref.getString("userEmail", null);
        restaurantName = pref.getString("RestaurantName", null);

        doubleOrderPrice = Double.parseDouble(orderPrice);

        sample=new ArrayList<String>(setOrder);

        //Adapter - connects to the arraylist
        orderItemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, sample);

        //Firebase
        myFB = FirebaseDatabase.getInstance();
        myRef = myFB.getReference("MunchiesDB").child("RestaurantOrders").child(restaurantName).child("Purchases").child("AuthUser: " + userEmail);
        myRef2 = myFB.getReference("MunchiesDB").child("OrderHistory").child(userEmail);


        //Views
        order = (ListView)findViewById(R.id.order);
        price = (TextView) findViewById(R.id.orderPrice);
        checkout_Btn = (Button) findViewById(R.id.checkout_Btn);
        remove_btn = (Button) findViewById(R.id.remove_Btn);

        //Modify Views
        order.setAdapter(orderItemsAdapter);

        //Listeners
        checkout_Btn.setOnClickListener(this);

        //Other
        price.setText("TOTAL: " + orderPrice);

         day = new SimpleDateFormat("yyyy-MM-dd");
         hour = new SimpleDateFormat("HH:mm");
         RemoveItem();


    }

    @Override
    public void onClick(View view){
        if (view == checkout_Btn){
            placeOrder();
        }

    }



    public void placeOrder(){
    currentDay = day.format(new Date());
    currentHour = hour.format(new Date());
    //Replace Num with KEY
    Random r = new Random();
    int n = r.nextInt(99999);

    String num = "Order: " + n;
    //TEST FOR DUPLICATION IF THERE IS TIME (Although rare)
    OrderHistoryClass order = new OrderHistoryClass(num, currentDay, orderPrice, sample.toString());



      myRef.child(currentDay).child(num).child("AuthUser: " + userEmail).child("Order").child("Approval").setValue("Awaiting");

       // myRef.child(currentDay).child(num).child("AuthUser: " + userEmail).child("Order").child("Items").setValue(sample);
        myRef.child(currentDay).child(num).child("AuthUser: " + userEmail).child("Order").child("Items").setValue(sample);


        // myRef.child(currentDay).child(num).child("AuthUser: " + userEmail).child("Order").setValue(sample);
    myRef.child(currentDay).child(num).child("AuthUser: " + userEmail).child("Order").child("HourCreated").setValue(currentHour);
        editor.putString("currentDay", currentDay);
        editor.apply();


    Toast.makeText(this, num, Toast.LENGTH_SHORT).show();

    myRef.child(currentDay).child(num).child("AuthUser: " + userEmail).child("Order").child("Price").setValue(orderPrice);


    myRef.child(currentDay).child(num).child("Order").setValue(sample);
    myRef.child(currentDay).child(num).child("HourCreated").setValue(currentHour);
    myRef.child(currentDay).child(num).child("price").setValue(orderPrice);
    myRef2.child(num).setValue(order);

    Toast.makeText(this, num, Toast.LENGTH_SHORT).show();

    if (checkout_Btn.isEnabled() && !userEmail.isEmpty()){
        checkout_Btn.setEnabled(false);
    }

    }
    public void RemoveItem() {
        order.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                orderitem = String.valueOf(parent.getItemAtPosition(position));
                String[] parts = orderitem.split("-"); //Array, each element is text between a dash
                double parsePrice = Double.parseDouble(parts[1]);
                doubleOrderPrice-=parsePrice;
                finalPrice = Double.toString(doubleOrderPrice);
                editor.putString("orderPrice", finalPrice);
                editor.apply();
                setOrder.remove(orderitem);
                Toast.makeText(RestaurantCustomerOrder.this, "Removed " + orderitem + "Parsed Price: "+ parsePrice, Toast.LENGTH_SHORT).show();
                recreate();
                return true;
            }
        });

    }
}





//    public void deleteMenuItem(){
//        final RestaurantItemClass restaurant = new RestaurantItemClass();
//        restaurantMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                restaurant.setItemID(restaurantMenuArrayList.get(position));
//                editRestaurantMenuName.setText(restaurantMenuArrayList.get(position));
//            }
//        });
//
//        btnDeleteMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String name = restaurant.getItemID();
//                Toast.makeText(getApplicationContext(), "Deleted " +  name  + " item menu", Toast.LENGTH_SHORT).show();
//                if(name.equals("")){
//                    Toast.makeText( RestaurantMenuCRUD.this, "Please Select item before delete!", Toast.LENGTH_LONG).show();
//                } else {
//                    myRef.child(restaurantName).child(name).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            myRef.child(name).removeValue();
//                            editRestaurantMenuName.setText("");
//                            editRestaurantMenuPrice.setText("");
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                        }
//                    });
//                }
//            }
//        });
//    }

//Prevents user from moving back
//    @Override
//    public void onBackPressed() {
//
//    }
