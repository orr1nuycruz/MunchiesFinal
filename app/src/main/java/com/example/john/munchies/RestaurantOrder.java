package com.example.john.munchies;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class RestaurantOrder extends AppCompatActivity implements View.OnClickListener {

    ListView order;
    TextView price;
    Button checkout_Btn;
    List<String> sample;
    String userEmail;
    SimpleDateFormat day;
    SimpleDateFormat hour;

    String currentDay;
    String currentHour;

    String orderPrice;
    String restaurantName;

    private ArrayAdapter<String> orderItemsAdapter;

    FirebaseDatabase myFB;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)

    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_order);
        // Shared Preferences

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Set<String> setOrder = pref.getStringSet("order",null);
       orderPrice = pref.getString("orderPrice", null);
         userEmail = pref.getString("userEmail", null);
        restaurantName = pref.getString("RestaurantName", null);

        sample=new ArrayList<String>(setOrder);

        //Adapter - connects to the arraylist
        orderItemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, sample);

        //Firebase
        myFB = FirebaseDatabase.getInstance();

        myRef = myFB.getReference("MunchiesDB").child("RestaurantOrders").child(restaurantName).child("Purchases").child("AuthUser: " + userEmail);


        //Views
        order = (ListView)findViewById(R.id.order);
        price = (TextView) findViewById(R.id.orderPrice);
        checkout_Btn = (Button) findViewById(R.id.checkout_Btn);

        //Modify Views
        order.setAdapter(orderItemsAdapter);
        price.setText("TOTAL: " + orderPrice);

        //Listeners
        checkout_Btn.setOnClickListener(this);

        //Other

         day = new SimpleDateFormat("yyyy-MM-dd");
         hour = new SimpleDateFormat("HH:mm");



    }


@Override

public void onClick(View view){
        if(view==checkout_Btn){
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

    myRef.child(currentDay).child(num).child("Order").setValue(sample);
    myRef.child(currentDay).child(num).child("HourCreated").setValue(currentHour);

    Toast.makeText(this, num, Toast.LENGTH_SHORT).show();

    myRef.child(currentDay).child(num).child("price").setValue(orderPrice);

    if (checkout_Btn.isEnabled() && !userEmail.isEmpty()){
        checkout_Btn.setEnabled(false);

    }


}

//Prevents user from moving back
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}



